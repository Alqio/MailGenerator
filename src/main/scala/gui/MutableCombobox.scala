package gui

import scala.swing.{ Component, Swing, event }
import javax.swing.JComboBox

class MutableComboBox[T] extends Component {
  override lazy val peer = new JComboBox[T]() with SuperMixin

  peer.addActionListener(Swing.ActionListener { e =>
    publish(event.SelectionChanged(MutableComboBox.this))
  })

  def items_=(s: Seq[T]) {
    peer.removeAllItems
    s.map(peer.addItem)
  }
  def items = (0 until peer.getItemCount()).map(peer.getItemAt)

  def index: Int = peer.getSelectedIndex
  def index_=(n: Int) { peer.setSelectedIndex(n) }
  def item: T = peer.getSelectedItem.asInstanceOf[T]
  def item_=(a: T) { peer.setSelectedItem(a) }

}