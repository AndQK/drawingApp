
import scala.math._
import java.awt.event.{MouseEvent, MouseListener, MouseMotionListener}
import java.awt.{Color, Graphics, Graphics2D, Image, RenderingHints, event}
import scala.collection.mutable._
import scala.swing.Component

class DrawSpace extends Component with MouseListener with MouseMotionListener {

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
         shapes += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line")
         repaint()
         oldX = currentX
         oldY = currentY
       } else if (shape == "Circle") {
         //g2.get.drawOval(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX))
         preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX), currentColor, "Circle")
         repaint()

       } else if (shape == "Rectangle") {
         //g2.get.drawRect(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY))
         preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Rectangle")
         repaint()

       } else if (shape == "Ellipse") {
         //g2.get.drawOval(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY))
         preview += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Ellipse")
         repaint()

        }

   }

  override def mouseReleased(e: MouseEvent) = {
    if (shape == "Line" && isDragged) {
      shapes += new Shape(oldX, oldY, currentX, currentY, currentColor, "Line")
    }
    else if (shape == "Ellipse" && isDragged) {
      shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Ellipse")
    }
    else if (shape == "Circle" && isDragged) {
      shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentX - oldX), currentColor, "Circle")
    }
    else if (shape == "Rectangle" && isDragged) {
      shapes += new Shape(oldX, oldY, abs(currentX - oldX), abs(currentY - oldY), currentColor, "Rectangle")
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

      if (element.shape == "Line") {
        graphics.drawLine(element.oldX, element.oldY, element.currentX, element.currentY)
      } else if (element.shape == "Ellipse") {
        graphics.drawOval(element.oldX, element.oldY, element.currentX, element.currentY)
      } else if (element.shape == "Circle") {
        graphics.drawOval(element.oldX, element.oldY, element.currentX, element.currentY)
      } else if (element.shape == "Rectangle") {
        graphics.drawRect(element.oldX, element.oldY, element.currentX, element.currentY)
      }
    }
      while (preview.nonEmpty) {
        val prev = preview.last
        preview -= prev
        g2.get.setPaint(prev.color)
        if (prev.shape == "Line") {
          g2.get.drawLine(prev.oldX, prev.oldY, prev.currentX, prev.currentY)

        } else if (prev.shape == "Ellipse") {
          g2.get.drawOval(prev.oldX, prev.oldY, prev.currentX, prev.currentY)

        } else if (prev.shape == "Circle") {
          g2.get.drawOval(prev.oldX, prev.oldY, prev.currentX, prev.currentY)

        } else if (prev.shape == "Rectangle") {
          g2.get.drawRect(prev.oldX, prev.oldY, prev.currentX, prev.currentY)

        }

      }
  }
  // public methods for GUI components
  def clear() = {
    g2.get.setPaint(Color.white)
    g2.get.fillRect(0, 0, peer.getSize().width, peer.getSize().height)
    g2.get.setPaint(currentColor)
    repaint()

  }
  // changes the colour of the graphics compnent
  def setColor(color: Color) = {
    currentColor = color
    g2.get.setPaint(color)
  }
  // changes the shape we want to draw
  def changeShape(shape: String) = {
    this.shape = shape
  }


}
