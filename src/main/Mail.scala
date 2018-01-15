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
  	var str = ""
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
	
	def createFromString(str: String): Mail = {
			
		
		???
	}
	
}