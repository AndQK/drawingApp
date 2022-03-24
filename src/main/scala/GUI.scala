import scala.swing._
import scala.collection.mutable._

  object GUI extends SimpleSwingApplication {

 // STRINGS for drop down boxes
  val colours = Seq("Black", "Red", "Green", "Orange", "Yellow", "Purple", "Brown", "Blue")
  val shapes  = Seq("Circle", "Rectangle", "Ellipse", "Line")

  // Creates the Frame with buttons
  def top =  new MainFrame {

    title    = "DrawingApp"
    resizable = false


    minimumSize   = new Dimension(1000, 1000)
    preferredSize = new Dimension(1000, 1000)
    maximumSize   = new Dimension(1000, 1000)


    val drawingScreen: Component = new DrawSpace
    drawingScreen.preferredSize = new Dimension(250, 250)
    drawingScreen.minimumSize = new Dimension(250, 250)
    drawingScreen.maximumSize = new Dimension(250, 250)

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
      preferredSize = new Dimension(250, 250)
      minimumSize = new Dimension(250, 250)
      maximumSize = new Dimension(250, 250)
      contents += drawingScreen
    }
    contents = buttons
    contents = drawPanel
    listenTo(drawingScreen.mouse.clicks)
  }
}


