
import scala.math._
import java.awt.event.{MouseEvent, MouseListener, MouseMotionListener}
import java.awt.{Color, Graphics2D, Image, RenderingHints, event}
import scala.collection.mutable._
import scala.swing.Component

class DrawSpace extends Component with MouseListener with MouseMotionListener {

  // Every shape has its own group number. This is needed when undoing sketchings by pencil.
  var groupNum = 1

  // MouseListeners for drawSpace component
  this.peer.addMouseListener(this)
  this.peer.addMouseMotionListener(this)

  // variables which store the information of selected parameters from drop-down lists
  private var shape: String = "Line"

  private var currentColor: Color = Color.black

  // stores the shapes
  private var shapes = Buffer[Shape]()

  // method for getting shapes for saving data of the shapes.
  def getShapes = this.shapes

  // stores the preview of the shapes
  private val preview = Buffer[Shape]()
  // stores the information of deleted shapes before new changes
  private val deleted = Buffer[Shape]()

  private var image: Option[Image] = None

  private var g2: Option[Graphics2D] = None

  private var currentX = 0
  private var currentY = 0
  private var oldX = 0
  private var oldY = 0

  // Boolean for checking if mouse is dragged
  private var isDragged = false


  peer.setDoubleBuffered(false)
    override def mousePressed(e: event.MouseEvent) = {
      oldX = e.getX
      oldY = e.getY
      }

     override def mouseDragged(e: event.MouseEvent) = {
       currentX = e.getX
       currentY = e.getY
       isDragged = true
       shape match {
         case "Pen"       => {
                             shapes += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line", groupNum)
                             oldX = currentX
                             oldY = currentY
         }
         case "Line"      => preview += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line", 0)
         case "Circle"    => preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX), currentColor, "Circle", 0)
         case "Rectangle" => preview += new Shape(min(oldX, currentX), min(oldY, currentY), abs(currentX - oldX), abs(currentY - oldY), currentColor, "Rectangle", 0)
         case "Ellipse"   => preview += new Shape(min(oldX, currentX), min(oldY, currentY), abs(currentX - oldX), abs(currentY - oldY), currentColor, "Ellipse", 0)
         case _           => // does nothing
       }
       repaint()
   }

  override def mouseReleased(e: MouseEvent) = {
    groupNum += 1
    if (isDragged) {
      shape match {
        case "Line"      => shapes += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line", 0)
        case "Circle"    => shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX), currentColor, "Circle", 0)
        case "Rectangle" => shapes += new Shape(min(oldX, currentX), min(oldY, currentY), abs(currentX - oldX), abs(currentY - oldY), currentColor, "Rectangle", 0)
        case "Ellipse"   => shapes += new Shape(min(oldX, currentX), min(oldY, currentY), abs(currentX - oldX), abs(currentY - oldY), currentColor, "Ellipse", 0)
        case _           => // does nothing
      }
    }
    isDragged = false
    deleted.clear
    repaint()
  }

  override def mouseMoved(e: event.MouseEvent) = {
    // does nothing
  }

  override def mouseClicked(e: event.MouseEvent) = {
    // Does nothing
  }

  override def mouseEntered(e: MouseEvent) = {
    // does nothing
  }

  override def mouseExited(e: MouseEvent) = {
    // does nothing
  }


   override def paintComponent(g: Graphics2D) = {
    if (image.isEmpty) {
      image = Some(peer.createImage(peer.getSize().width, peer.getSize().height))
      g2 = Some(image.get.getGraphics.asInstanceOf[Graphics2D])
      g2.get.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      clear()
    }
    g.drawImage(image.get, 0, 0, null)
    var graphics = g
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON)

    for (element <- shapes) {
       graphics.setPaint(element.color)
       element.shape match {
         case "Line"      => graphics.drawLine(element.oldX, element.oldY, element.currentX, element.currentY)
         case "Ellipse"   => graphics.drawOval(element.oldX, element.oldY, element.currentX, element.currentY)
         case "Circle"    => graphics.drawOval(element.oldX, element.oldY, element.currentX, element.currentY)
         case "Rectangle" => graphics.drawRect(element.oldX, element.oldY, element.currentX, element.currentY)
         case _           => // does nothing
       }
    }
      if (preview.nonEmpty) {
        val prev = preview.last
        preview.clear
        graphics.setPaint(prev.color)
        prev.shape match {
          case "Line"      => graphics.drawLine(prev.oldX, prev.oldY, prev.currentX, prev.currentY)
          case "Ellipse"   => graphics.drawOval(prev.oldX, prev.oldY, prev.currentX, prev.currentY)
          case "Circle"    => graphics.drawOval(prev.oldX, prev.oldY, prev.currentX, prev.currentY)
          case "Rectangle" => graphics.drawRect(prev.oldX, prev.oldY, prev.currentX, prev.currentY)
          case _           => // does nothing
        }
      }
  }
  // public methods for GUI components
  def clear() = {
    g2.get.setPaint(Color.white)
    g2.get.fillRect(0, 0, peer.getSize().width, peer.getSize().height)
    shapes.clear
    deleted.clear
    repaint()
    g2.get.setPaint(currentColor)

  }
  // changes the colour of the graphics component
  def setColor(color: Color) = {
    currentColor = color
    g2.get.setPaint(color)
  }
  // changes the shape we want to draw
  def changeShape(shape: String) = {
    this.shape = shape
  }

  // method for undoing the image.
  def undo() = {
    if (shapes.nonEmpty) {
      if (shapes.last.group != 0) {
        val firstOut = shapes.last
        shapes -= firstOut
        deleted += firstOut
        while (shapes.nonEmpty && shapes.last.group == firstOut.group) {
          val removed = shapes.last
          deleted += removed
          shapes -= removed
          repaint()
          }
      } else if (shapes.last.group == 0) {
        deleted += shapes.last
        shapes -= shapes.last
        repaint()
      }

    }
  }
    // method for redoing the image.
  def redo() = {
    if (deleted.nonEmpty) {
      if (deleted.last.group != 0) {
        val firstout = deleted.last
        shapes += firstout
        deleted -= firstout
        while (deleted.nonEmpty && deleted.last.group == firstout.group) {
          shapes += deleted.last
          deleted -= deleted.last
          repaint()
        }
      } else if (deleted.last.group == 0) {
        shapes += deleted.last
        deleted -= deleted.last
        repaint()
      }
    }

  }

  // method for loading shapes
  def loadShapes(loaded: Buffer[Shape]) = {
    shapes = loaded
    repaint()
  }


}
