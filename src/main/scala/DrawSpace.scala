
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
  private val shapes = Buffer[Shape]()
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
       if (shape == "Pen") {
         //g2.get.drawLine(oldX, oldY, currentX, currentY)
         shapes += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line", groupNum)
         repaint()
         oldX = currentX
         oldY = currentY
       } else if (shape == "Line") {
         preview += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line", 0)
         repaint()
       } else if (shape == "Circle") {
         //g2.get.drawOval(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX))
         preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX), currentColor, "Circle", 0)
         repaint()

       } else if (shape == "Rectangle") {
         //g2.get.drawRect(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY))
         preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Rectangle", 0)
         repaint()

       } else if (shape == "Ellipse") {
         //g2.get.drawOval(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY))
         preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Ellipse", 0)
         repaint()

        }

   }

  override def mouseReleased(e: MouseEvent) = {
    groupNum += 1
    if (shape == "Line" && isDragged) {
      shapes += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line", 0)
    }
    else if (shape == "Ellipse" && isDragged) {
      shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Ellipse", 0)
    }
    else if (shape == "Circle" && isDragged) {
      shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX), currentColor, "Circle", 0)
    }
    else if (shape == "Rectangle" && isDragged) {
      shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Rectangle", 0)
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




}
