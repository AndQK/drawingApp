import java.awt.Color
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import scala.swing.ComboBox

class ColorChanger(drawSpace: DrawSpace, combo: ComboBox[String]) extends ActionListener {

  override def actionPerformed(e: ActionEvent) = {

     combo.peer.getItemAt(combo.peer.getSelectedIndex) match {
       case "Black"     => drawSpace.setColor(Color.black)
       case "Red"       => drawSpace.setColor(Color.red)
       case "Green"     => drawSpace.setColor(Color.green)
       case "Orange"    => drawSpace.setColor(Color.orange)
       case "Yellow"    => drawSpace.setColor(Color.yellow)
       case "Brown"     => drawSpace.setColor( new Color(150, 75, 0))
       case "Blue"      => drawSpace.setColor(Color.blue)
       case "Pink"      => drawSpace.setColor(Color.pink)
       case "Magenta"   => drawSpace.setColor(Color.magenta)
       case "Cyan"      => drawSpace.setColor(Color.cyan)
       case "Gray"      => drawSpace.setColor(Color.gray)
       case "Dark_gray" => drawSpace.setColor(Color.darkGray)
       case "Lime"      => drawSpace.setColor( new Color(69, 229, 33))
       case "Violet"    => drawSpace.setColor( new Color(127, 0, 255))
       case "Salmon"    => drawSpace.setColor( new Color(255, 140, 105))
       case "Gold"      => drawSpace.setColor( new Color(212, 175, 55))
       case _           => // do nothing

    }
  }


}
class ShapeChanger(drawSpace: DrawSpace, combo: ComboBox[String]) extends ActionListener {

  override def actionPerformed(e: ActionEvent) = {
    combo.peer.getItemAt(combo.peer.getSelectedIndex) match {
      case "Line"      => drawSpace.changeShape("Line")
      case "Circle"    => drawSpace.changeShape("Circle")
      case "Rectangle" => drawSpace.changeShape("Rectangle")
      case "Ellipse"   => drawSpace.changeShape("Ellipse")
      case _           => // do nothing
    }
  }

}
