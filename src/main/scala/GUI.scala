import scala.swing._
import scala.collection.mutable._

  object GUI extends SimpleSwingApplication {

 // STRINGS for drop down boxes
  val colours = Seq("Black", "Red", "Green", "Orange", "Yellow", "Purple", "Brown", "Blue")
  val shapes  = Seq("Circle", "Rectangle", "Ellipse", "Line")

  // Creates the Frame with buttons
  def top =  new MainFrame {
    title    = "DrawingApp"
    contents = new FlowPanel {
      contents += new ComboBox(colours)
      contents += new ComboBox(shapes)
      contents += new Button("Pen")
      contents += new Button("undo")
      contents += new Button("text")
      contents += new Button("choose")
      contents += new Button("Clear")
      contents += new Button("save")
    }
    size     = new Dimension(600, 600)

  }

}


