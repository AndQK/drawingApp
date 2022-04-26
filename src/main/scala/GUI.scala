import java.awt.event.ActionListener
import scala.swing._
import scala.collection.mutable._

object GUI extends SimpleSwingApplication {

 // STRINGS for drop down boxes
  val colours = Seq("Black", "Red", "Green", "Orange", "Yellow", "Brown", "Blue", "Pink", "Magenta", "Cyan", "Gray", "Dark_gray", "Lime", "Violet", "Salmon", "Gold")
  val shapes  = Seq("Line", "Circle", "Rectangle", "Ellipse")

  // Creates the Frame with buttons
  def top =  new MainFrame {

    title    = "DrawingApp"
    resizable = false
    val chooser = new FileChooser()
    // private method for buttons to handle file choosing and saving or loading to it.
    private def loadOrSave(name: String) = {

      if (name == "Save") {
        val returnVal = chooser.showSaveDialog(this)
        returnVal.toString match {
          case "Approve" => val file: java.io.File = chooser.selectedFile; DrawReader.saveDrawing(file.toString, drawingScreen)
          case "Cancel" => //just closes the dialog.
          case _        => // Does nothing
        }

      } else if (name == "Load") {
        val returnVal = chooser.showOpenDialog(this)
          returnVal.toString match {
            case "Approve" => val file: java.io.File = chooser.selectedFile; DrawReader.loadDrawing(file.toString, drawingScreen)
            case "Cancel" => //just closes the dialog.
            case _        => // Does nothing
          }
      }

    }

    minimumSize   = new Dimension(700, 700)
    preferredSize = new Dimension(700, 700)
    maximumSize   = new Dimension(700, 700)

    // DrawSpace instance for drawing
    val drawingScreen = new DrawSpace
    drawingScreen.preferredSize = new Dimension(670, 600)
    drawingScreen.minimumSize = new Dimension(670, 600)
    drawingScreen.maximumSize = new Dimension(670, 600)

    //drop-down list for colours
    val colorList = new ComboBox(colours)
    // drop-down list for shapes
    val shapeList = new ComboBox(shapes)

    // action listener for colorList
    val colorChanger: ActionListener = new ColorChanger(drawingScreen, colorList)
    colorList.peer.addActionListener(colorChanger)

    // action listener for shapeList
    val shapeChanger: ActionListener = new ShapeChanger(drawingScreen, shapeList)
    shapeList.peer.addActionListener(shapeChanger)


    // buttons for different actions
    val PenBtn = new Button("Pen")
    PenBtn.peer.addActionListener(e => drawingScreen.changeShape("Pen"))

    val undoBtn = new Button("Undo")
    undoBtn.peer.addActionListener(e => drawingScreen.undo())

    val redoBtn = new Button("redo")
    redoBtn.peer.addActionListener(e => drawingScreen.redo())

    val loadBtn = new Button("Load")
    loadBtn.peer.addActionListener(e => loadOrSave("Load"))

    val clearBtn = new Button("Clear")
    clearBtn.peer.addActionListener(e => drawingScreen.clear())
    val saveBtn = new Button("Save")
    saveBtn.peer.addActionListener(e => loadOrSave("Save"))

    // buttons which we use to switch between settings
    val buttons = new FlowPanel {
      contents += colorList
      contents += shapeList
      contents += PenBtn
      contents += undoBtn
      contents += redoBtn
      contents += loadBtn
      contents += clearBtn
      contents += saveBtn
    }
    val drawPanel = new FlowPanel {
      contents += drawingScreen
    }
    contents = new BoxPanel(Orientation.Vertical) {
      contents += buttons
      contents += drawPanel

    }

  }
}


