
import java.awt.event.{MouseAdapter, MouseMotionAdapter}
import java.awt.{Color, Graphics2D, Image, RenderingHints, event}
import scala.swing.Component

class DrawSpace extends Component {


  private var image: Option[Image] = None

  private var g2: Option[Graphics2D] = None

  private var currentX = 0
  private var currentY = 0
  private var oldX = 0
  private var oldY = 0
  peer.setDoubleBuffered(false)
  peer.addMouseListener(new MouseAdapter {
    override def mousePressed(e: event.MouseEvent) = {
      oldX = e.getX
      oldY = e.getY
      }
    })
   peer.addMouseMotionListener(new MouseMotionAdapter {
     override def mouseDragged(e: event.MouseEvent) = {
       currentX = e.getX
       currentY = e.getY

       if (g2.isDefined) {
         g2.get.drawLine(oldX, oldY, currentX, currentY)
         repaint()
         oldX = currentX
         oldY = currentY
       }
     }
   })

   override def paintComponent(g: Graphics2D) = {
    if (image.isEmpty) {
      image = Some(peer.createImage(peer.getSize().width, peer.getSize().height))
      g2 = Some(image.get.getGraphics.asInstanceOf[Graphics2D])
      g2.get.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      clear()
    }
    g.drawImage(image.get, 0, 0, null)
  }

  def clear() = {
    g2.get.setPaint(Color.white)
    g2.get.fillRect(0, 0, peer.getSize().width, peer.getSize().height)
    g2.get.setPaint(Color.black)
    repaint()

  }

}
