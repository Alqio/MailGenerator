package main

class Mail {
  
  val kalenteri = new Kalenteri()
  val kilta = new Topic("Kilta")
  val ayy = new Topic("AYY & Muut")
  val pohjanurkkaus = new Topic("Pohjanurkkaus")
  
  val topicChangeMark = "----"
  
  val topics = Vector(kalenteri, kilta, ayy, pohjanurkkaus)
  
  def addSubtopicToTopic(topic: Topic, subtopic: Subtopic) = {
  	topic.addSubtopic(subtopic)
  	kalenteri.addSubtopic(subtopic)
  }
  
  def generateTableOfContents = {
  	var str = "Sisällysluettelo\n"
  	for (i <- 0 until topics.size) {
  		str += topics(i).generateTableOfContents(i + 1)
  	}
  	str += "\n" + topicChangeMark
  	str
  }
  
  def generate = {
  	var str = ""
  	for (i <- topics.indices) {
  		str += topics(i).generate(i + 1)
  	}
  	str
  }
  
  def generateAll = {
  	this.generateTableOfContents + generate
  }
  
  
}
object Mail {
	
	def createFromString(lines: Array[String]): Mail = {
		
		val mail = new Mail
		
		var text = lines.clone
		//Drop until we are the first subtopic
		text = text.dropWhile(_ != "2. Kilta").drop(1).dropWhile(_ != "2. Kilta").drop(2)
		
		var topic = mail.kilta
		
		try {
			while (text(1) != "" && text(1) != "\n") {
				addSubtopic(topic)
			}
		} catch {
			case ex: Exception => {
				println(text.mkString("\n"))
				//println(mail.generateAll)
			}
		}
		
		
		def addSubtopic(topic: Topic) = {
			val name = text(0).dropWhile(_ != ' ').drop(1).takeWhile(!_.isDigit).trim
			
			val datestr = text(0).dropWhile(_ != ' ').drop(name.length + 1).trim
			
			val date = try {
				Date(datestr)
			} catch {
				case ex: Exception => Date(30,12)
			}
			//text = text.drop(1)
			val subtopicText = text.takeWhile(row => row != "---" && row != "----")
			text = text.drop(subtopicText.length)
			
			val subtopic = new Subtopic(name, date)
			subtopic.text = subtopicText.mkString("\n")
			
			mail.addSubtopicToTopic(topic, subtopic)
			//println(text.take(50).mkString("\n"))	
		}
		
		println("@@@@@@@@@@")
		println(text.mkString("\n"))
		
		mail
	}
	
}