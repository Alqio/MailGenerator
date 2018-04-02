package gui

import scala.swing._
import scala.swing.event._
import javax.swing.UIManager
import main._
import java.io.File
import javax.swing.JFileChooser
import java.io.FileReader
import scala.io.Source
import java.io._

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
	window.title = "Viikkomaili text"
	
	def updateOutput() = {
		window.output.text = mail.generateAll
	}
	
	val mainFrame = new MainFrame {
		
		val subtopicName  = new TextField(40)
		val topic = new MutableComboBox[Topic]()
		topic.items = mail.topics.drop(1)
		
		val subtopic = new MutableComboBox[Subtopic]()
		
		subtopic.items = topic.item.subtopics
		subtopic.preferredSize = new Dimension(100,20)
		
		val date  = new TextField(10)
		val signup_start = new TextField(10)
		val signup_end = new TextField(10)
		
		val link  = new TextField(40)
		val text  = new TextArea(10, 80) {
			editable = true
		}
		val addButton = new Button("Add subtopic")
		val loadSubtopicButton = new Button("Select subtopic")
		val loadButton = new Button("Load from file")
		val saveButton = new Button("Save to a file")
		val loadSubtopics = new Button("Load subtopics")
		val removeSubtopic = new Button("Remove subtopic")
		val htmlCheckbox = new CheckBox("HTML?")
		
		
		val items = Array(subtopicName, topic, date, link, text, addButton,
		    loadButton, saveButton, loadSubtopicButton, subtopic, topic, 
		    loadSubtopics, removeSubtopic, htmlCheckbox, signup_start, signup_end)

		items.foreach(this.listenTo(_))
		
		this.reactions += {
			case press: ButtonClicked => {
				val source = press.source
				if (source == addButton) {
					
					if (subtopicName.text != "" && date.text != "" && text.text != "") {
						
						val st = new Subtopic(subtopicName.text, Date(date.text), link.text)
						st.text = text.text
						
						if (signup_start != "" && signup_end != "") {
							st.signup_start = Date(signup_start.text)
							st.signup_end = Date(signup_end.text)
							
						}
						
						mail.addSubtopicToTopic(topic.item, st)
						subtopic.items = topic.item.subtopics
						
						updateOutput()
						//println(topic.item.subtopics)
						println("Added subtopic " + subtopicName.text + " to topic " + topic.item)
						updateOutput()
					}
				} else if (source == loadButton) {
					loadFile()
					updateOutput()
				} else if (source == saveButton) {
					saveFile()
					updateOutput()
				} else if (source == loadSubtopicButton) {
					val st = subtopic.item
					date.text = st.date.toString
					signup_start.text = st.signup_start.toString
					signup_end.text = st.signup_end.toString
					link.text = st.link
					text.text = st.text
					subtopicName.text = st.name
				} else if (source == loadSubtopics) {
				  subtopic.items = topic.item.sortSubtopics
				} else if (source == removeSubtopic) {
				  val sbs = topic.item.subtopics
				  mail.removeSubtopicFromTopic(topic.item, subtopic.item)
				  updateOutput()
				} else if (source == htmlCheckbox) {
					mail.html = !mail.html
				}
			}
		}
		
		def saveFile() = {
			val workingDirectory = new File(System.getProperty("user.dir"));
	
			val saveFile = new JFileChooser()
			saveFile.setCurrentDirectory(workingDirectory)
			val ret = saveFile.showSaveDialog(null)
			
			val file = try {
				val fl = saveFile.getSelectedFile()
				if (fl == null) None else Some(fl)
			} catch {
				case ex: Exception => {
				  ex.printStackTrace()
				  None
				}
			}
			
			if (file.isEmpty) {
			  println("Error occurred when saving file, nothing saved.")
			} else {
			  val pw = new PrintWriter(file.get, "UTF-8")
			  pw.write(mail.generateAll)
			  pw.close()
			  println("File saved successfully! File name: " + file.get.getName())
			}
			
		}

		
		def loadFile(): Unit = {
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
			
			if (file.isEmpty) return 
			
			val path = file.get.getAbsolutePath()
			
      val bufferedSource = Source.fromFile(path)
      val lines = bufferedSource.getLines().toArray
      bufferedSource.close
      //println(lines.mkString("\n"))
			
      mail = Mail.createFromString(lines)
      topic.items = mail.topics.drop(1)
      updateOutput()
		}
		
		this.contents = new GridBagPanel { 
      import scala.swing.GridBagPanel.Anchor._
      import scala.swing.GridBagPanel.Fill
      layout += new Label("Topic:") -> new Constraints(0, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += new Label("Subtopic:") -> new Constraints(3, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += new Label("Name:")  -> new Constraints(0, 1, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Date:")  -> new Constraints(0, 3, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Signup start:")  -> new Constraints(0, 4, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Signup End:")  -> new Constraints(0, 5, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Link:")  -> new Constraints(0, 2, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += new Label("Text:")  -> new Constraints(0, 6, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      
      layout += addButton				  	-> new Constraints(0, 7, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += loadSubtopicButton  -> new Constraints(1, 7, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += saveButton					-> new Constraints(3, 7, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += loadButton					-> new Constraints(4, 7, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += removeSubtopic			-> new Constraints(2, 7, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      layout += htmlCheckbox	  		-> new Constraints(5, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(8, 5, 5, 5), 0, 0)
      
      layout += topic 							-> new Constraints(1, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += loadSubtopics 			-> new Constraints(2, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += subtopic 					  -> new Constraints(4, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += subtopicName				-> new Constraints(1, 1, 4, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += date  							-> new Constraints(1, 3, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += signup_start				-> new Constraints(1, 4, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += signup_end 					-> new Constraints(1, 5, 1, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += link  							-> new Constraints(1, 2, 4, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += text  							-> new Constraints(1, 6, 4, 1, 0, 1, NorthWest.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
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