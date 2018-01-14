package gui

import scala.swing._
object test extends SimpleSwingApplication {

	object messages extends TextArea(rows = 40, columns = 60) 

	val frame = new MainFrame {
		contents = new FlowPanel {
			val outputTextScrollPane = new ScrollPane(messages)
			contents += outputTextScrollPane
		}
	}
	def top = frame
}