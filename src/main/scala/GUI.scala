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


    minimumSize   = new Dimension(700, 700)
    preferredSize = new Dimension(700, 700)
    maximumSize   = new Dimension(700, 700)

    // DrawSpace instance for drawing
    val drawingScreen = new DrawSpace
    drawingScreen.preferredSize = new Dimension(500, 500)
    drawingScreen.minimumSize = new Dimension(500, 500)
    drawingScreen.maximumSize = new Dimension(500, 500)

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

    val textBtn = new Button("Text")

    val loadBtn = new Button("Load")

    val clearBtn = new Button("Clear")
    clearBtn.peer.addActionListener(e => drawingScreen.clear())
    val saveBtn = new Button("Save")

    // buttons which we use to switch between settings
    val buttons = new FlowPanel {
      contents += colorList
      contents += shapeList
      contents += PenBtn
      contents += undoBtn
      contents += textBtn
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


