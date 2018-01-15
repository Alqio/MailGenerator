package gui

import scala.swing._
import scala.swing.event._
import javax.swing.UIManager
import main._
import java.io.File
import javax.swing.JFileChooser
import java.io.FileReader
import scala.io.Source

object View extends SimpleSwingApplication {
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

	var mail = new Mail

	val window = new Frame {
		val output = new TextArea() {
			rows = 40
			columns = 80
			editable = false
		}
		
		output.text = mail.generateAll
		
		contents = new FlowPanel {
			contents += new ScrollPane(output)
		}
	}
	
	updateOutput()
	
	window.visible = true
	
	def updateOutput() = {
		window.output.text = mail.generateAll
	}
	
	val mainFrame = new MainFrame {
		
		val subtopicName  = new TextField(40)
		val topic = new ComboBox(mail.topics)
		val date  = new TextField(40)
		val link  = new TextField(40)
		val text  = new TextArea(10, 80) {
			editable = true
		}
		val addButton = new Button("Add subtopic")
		val loadButton = new Button("Load")
		val saveButton = new Button("Save")

		
		
		val items = Array(subtopicName, topic, date, link, text, addButton, loadButton, saveButton)

		items.foreach(this.listenTo(_))
		
		this.reactions += {
			case press: ButtonClicked => {
				val source = press.source
				if (source == addButton) {
					
					if (subtopicName.text != "" && date.text != "" && text.text != "") {
						val subtopic = new Subtopic(subtopicName.text, Date(date.text), link.text)
						subtopic.text = text.text
					
						mail.addSubtopicToTopic(topic.selection.item, subtopic)
						
						updateOutput()
					}
				} else if (source == loadButton) {
					val f = loadFile()
					mail = Mail.createFromString(f)
					updateOutput()
				} else if (source == saveButton) {
					saveFile()
				}
			}
		}
		

		
		def saveFile(): Option[File] = {
			val workingDirectory = new File(System.getProperty("user.dir"));
	
			val saveFile = new JFileChooser()
			saveFile.setCurrentDirectory(workingDirectory)
			val ret = saveFile.showSaveDialog(null)
			try {
				val fl = saveFile.getSelectedFile()
				if (fl == null) None else Some(fl)
			} catch {
				case ex: Exception => None
			}

		}

		
		def loadFile(): String = {
			val workingDirectory = new File(System.getProperty("user.dir"));
	
			val openFile = new JFileChooser()
			openFile.setCurrentDirectory(workingDirectory)
			val ret = openFile.showOpenDialog(null)
			
			val file = try {
				val fl = openFile.getSelectedFile()
				if (fl == null) None else Some(fl)
			} catch {
				case ex: Exception => None
			}
			
			if (file.isEmpty) return ""
			
			val path = file.get.getAbsolutePath()
			
			Source.fromFile(path).getLines.mkString
					
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
      layout += saveButton					-> new Constraints(1, 5, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += loadButton					-> new Constraints(2, 5, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)

      layout += topic 							-> new Constraints(1, 0, 2, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += subtopicName				-> new Constraints(1, 1, 2, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += date  							-> new Constraints(1, 2, 2, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += link  							-> new Constraints(1, 3, 2, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += text  							-> new Constraints(1, 4, 2, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
    }
		
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
	def top = mainFrame
	mainFrame.peer.setLocationRelativeTo(null)
	window.peer.setLocationRelativeTo(mainFrame.peer)

}