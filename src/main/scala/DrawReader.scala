import java.awt.Color
import java.io.{BufferedReader, BufferedWriter, FileNotFoundException, FileReader, FileWriter, IOException}
import scala.collection.mutable.Buffer
import scala.util.{Failure, Success, Try}


class CorruptedDrawingFile(msg: String) extends Exception(msg) {
// custom exception class
}

object DrawReader {

  def saveDrawing(file: String, drawing: DrawSpace) = {
    val shapes = drawing.getShapes
    val fileContent = Buffer[String]()

    fileContent += "Shapes:"
    // loop for adding shape's information to fileContent
    for (shape <- shapes) {

      fileContent += "Type: " + shape.shape
      fileContent += "x-coordinate: " + shape.oldX
      fileContent += "y-coordinate: " + shape.oldY
      fileContent += "Width: " + shape.currentX
      fileContent += "Height: " + shape.currentY
      fileContent += "Color: " + shape.color.getRed + "," + shape.color.getGreen + "," + shape.color.getBlue
      fileContent += "Group: " + shape.group
      fileContent += "------"

    }

    fileContent += "EndOfFile"

    try {

      val writer = new FileWriter(file)
      val bufferedWriter = new BufferedWriter(writer)

      try {
          bufferedWriter.write(fileContent.mkString("\n"))
      } finally {
          bufferedWriter.close()
      }

    } catch {

      case _: FileNotFoundException => println("Unable to save the data: File not found")
      case _: IOException           => println("Unable to save the save the data: IOException")
      case _: Throwable             => println("Unable to save the data: Unexpected exception")
    }

  }

  def loadDrawing(file: String, drawing: DrawSpace) = {

    // Storage for shapes
    val shapes = Buffer[Shape]()

    try {

    val reader = new FileReader(file)
    val bufferedReader = new BufferedReader(reader)

    try {

      var currentLine = bufferedReader.readLine().trim.toLowerCase

      if (!currentLine.startsWith("shapes:")) {
        throw new CorruptedDrawingFile("Unknown file type")
      }
      if (currentLine == null) {
        throw new Exception("The file you want to load is Empty")
      }


      var data = Buffer[String]()
      while ({
        currentLine = bufferedReader.readLine()
        currentLine != null
      }) {
        data += currentLine.trim
      }
      if (!data.last.contains("EndOfFile")) {
        throw new CorruptedDrawingFile("Corrupted Drawing file")

      }

      data = data.dropRight(1)              // drops line: "EndOfFile"

      // stores each shapes data in different Buffer.
      val dataInChunks = Buffer[Buffer[String]]()
      while (data.nonEmpty) {
        val shapeData = data.takeWhile(_ != "------")
        if (shapeData.length == 7) {
          dataInChunks += shapeData
          for (info <- shapeData) {
          data -= info
          }
          data = data.drop(1)                                  //drops line: "------"
        } else {
          throw new CorruptedDrawingFile(s"Not enough parameters for creating a shape: Instead of 7 parameters you gave only ${shapeData.length}")
        }

      }

      // loop for creating saved shapes
      for (chunk <- dataInChunks) {
         val shape = shapeData(chunk)
         shape match {
           case Failure(e) => throw e
           case Success(e) => shapes += e
         }

      }
      // If everything was fine with the file we are now ready to load shapes into our DrawSpace object.
        drawing.loadShapes(shapes)

      } finally {
        bufferedReader.close()
      }
    } catch {
      case _: FileNotFoundException =>
            println("Error with loading a drawing: file not found.")
      case e: IOException =>
            println(e.getMessage)
      case e: CorruptedDrawingFile =>
            println(e.getMessage)
      case e: Throwable =>
            println(e.getMessage)

    }
  }


  // helper method for checking if shape's
  // data is not corrupted and if so brings back a Success that contains a shape and a Failure otherwise.

  private def shapeData(data: Buffer[String]): Try[Shape] = {
    // Extracting data from buffer
    // First we get type and coordinates
    val shapeType = data.collectFirst({case s: String if s.contains("Type:") => s.drop(5).trim})
    val x = data.collectFirst({case s: String if s.contains("x-coordinate:") => s.drop(13).trim.toIntOption}).flatten
    val y = data.collectFirst({case s: String if s.contains("y-coordinate:") => s.drop(13).trim.toIntOption}).flatten

    // then we get the size of the shape
    val width = data.collectFirst({case s: String if s.contains("Width:") => s.drop(6).trim.toIntOption}).flatten
    val height = data.collectFirst({case s: String if s.contains("Height:") => s.drop(7).trim.toIntOption}).flatten

    // Next we get color and red, green, blue components.
    val colorInStr = data.collectFirst({case s: String if s.contains("Color:") => s.drop(6).trim})
    val red = colorInStr.flatMap(s => s.split(',').headOption.flatMap(s => s.toIntOption))
    val green = colorInStr.flatMap(s => s.split(',').lift(1).flatMap(s => s.toIntOption))
    val blue = colorInStr.flatMap(s => s.split(',').lift(2).flatMap(s => s.toIntOption))

    // last bit is group index of the shape.
    val groupOp = data.collectFirst({case s: String if s.contains("Group:") => s.drop(6).trim.toIntOption}).flatten

    var shape: Option[Shape] = None

    for {
      name <- shapeType
      oldX <- x
      oldY <- y
      currentX <- width
      currentY <- height
      r <- red
      g <- green
      b <- blue
      group <- groupOp
    } {
      shape = Some(new Shape(oldX, oldY, currentX, currentY, new Color(r, g, b), name, group))
    }

    shape match {
      case None => Failure( new CorruptedDrawingFile("Wrong information in shape's data"))
      case Some(s) => Success(s)
    }
  }

}
