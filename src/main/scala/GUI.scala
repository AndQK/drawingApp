import scala.swing._
import scala.collection.mutable._
import scala.swing.event.{MouseDragged, MousePressed, MouseReleased}

object GUI extends SimpleSwingApplication {

 // STRINGS for drop down boxes
  val colours = Seq("Black", "Red", "Green", "Orange", "Yellow", "Purple", "Brown", "Blue")
  val shapes  = Seq("Circle", "Rectangle", "Ellipse", "Line")

  // Creates the Frame with buttons
  def top =  new MainFrame {

    title    = "DrawingApp"
    resizable = false


    minimumSize   = new Dimension(700, 700)
    preferredSize = new Dimension(700, 700)
    maximumSize   = new Dimension(700, 700)


    val drawingScreen: Component = new DrawSpace
    drawingScreen.preferredSize = new Dimension(500, 500)
    drawingScreen.minimumSize = new Dimension(500, 500)
    drawingScreen.maximumSize = new Dimension(500, 500)


    // buttons which we use to switch between settings
    val buttons = new FlowPanel {
      contents += new ComboBox(colours)
      contents += new ComboBox(shapes)
      contents += new Button("Pen")
      contents += new Button("undo")
      contents += new Button("text")
      contents += new Button("choose")
      contents += new Button("Clear")
      contents += new Button("save")
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


