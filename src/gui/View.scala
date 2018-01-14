package gui

import scala.swing._
import scala.swing.event._
import javax.swing.UIManager
import main._

object View extends SimpleSwingApplication {
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

	val mail = new Mail

	def top = new MainFrame {
		val output = new TextArea() {
			rows = 10
			columns = 80
			editable = false
			wordWrap = false
			lineWrap = false
		}
		
		output.text = mail.generateAll
		
		val flowpanel = new FlowPanel() {
			contents += new ScrollPane(output)
		}
		
		val subtopicName  = new TextField(40)
		val topic = new ComboBox(mail.topics)
		val date  = new TextField(40)
		val link  = new TextField(40)
		val text  = new TextArea(10, 80) {
			editable = true
		}
		val addButton = new Button("Add subtopic")

		val items = Array(subtopicName, topic, date, link, text, addButton)

		items.foreach(this.listenTo(_))
		
		this.reactions += {
			case press: ButtonClicked => {
				val source = press.source
				if (source == addButton) {
					
					if (subtopicName.text != "" && date.text != "" && text.text != "") {
						val subtopic = new Subtopic(subtopicName.text, Date(date.text), link.text)
						subtopic.text = text.text
					
						mail.addSubtopicToTopic(topic.selection.item, subtopic)
						
						output.text = mail.generateAll
					}
					
				}
			}
		}
		
		
		
		this.contents = new GridBagPanel { 
      import scala.swing.GridBagPanel.Anchor._
      import scala.swing.GridBagPanel.Fill
      layout += new Label("Topic:") -> new Constraints(0, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += new Label("Name:")  -> new Constraints(0, 1, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Date:")  -> new Constraints(0, 2, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Link:")  -> new Constraints(0, 3, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Text:")  -> new Constraints(0, 4, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      
      layout += addButton						-> new Constraints(0, 5, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)

      layout += new Label("Output") -> new Constraints(0, 6, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      
      layout += topic 							-> new Constraints(1, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += subtopicName				-> new Constraints(1, 1, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += date  							-> new Constraints(1, 2, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += link  							-> new Constraints(1, 3, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += text  							-> new Constraints(1, 4, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += flowpanel 				  -> new Constraints(1, 6, 2, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      

    }
		val j = new FlowPanel(output)
		
		this.menuBar = new MenuBar {
      contents += new Menu("Program") {
        val quitAction = Action("Quit") { dispose() }
        contents += new MenuItem(quitAction)
      }
    }  

		this.minimumSize = new Dimension(200, 200)
		this.pack()
    this.title = "Viikkomaili generator"
    
		
	}

}