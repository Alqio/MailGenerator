package main

class Mail {
  
  val kalenteri = new Topic("Kalenteri")
  val kilta = new Topic("Kilta")
  val ayy = new Topic("AYY & Muut")
  val pohjanurkkaus = new Topic("Pohjanurkkaus")
  
  val topicChangeMark = "----"
  
  val topics = Vector(kalenteri, kilta, ayy, pohjanurkkaus)
  
  
  val sub1 = new Subtopic("UraTiKAS", Date(18,1))
  val sub2 = new Subtopic("Tuxera Excu", Date(25,1), "tietokilta.fi")
  val sub3 = new Subtopic("aTalent Excu", Date(15,1), "facebook.com")
  sub1.text = """UraTiKAs tulee taas! 
UraTiKAs järjestetään 18.1.2018 T-talolla salissa T1 alkaen kello 14.00. Sen aikana 12 kesätoitäkin 
tarjoavaa firmaa esittelevät itsensä ja jokaisella firmalla on oma esittelypiste, jossa voi jutella 
firman edustajien kanssa. """
  sub2.text = """Join our Excu at Tuxera!

Hey the Tietokilta members! We are pleased to invite you to our excursion on Wednesday 
January 24 at 17:00 onwards. Come and learn on what Tuxera is about, see some of our 
coding practices, and hear career stories from our employees. During the evening, 
you will get a rare opportunity to visit our hardware-packed lab. 
We will also talk about future opportunities for students interested in, for example, 
Linux kernel, file systems, as well as storage and networking technologies.

Food, drinks, Super Bomberman and table tennis will be all on the house! 
You can also take advantage of our sauna and pool facilities if you wish. 
In that case, just remember to bring your bathing suit and towel."""
  sub3.text = """Are your LinkedIn profile and CV in top notch condition? Want to get some last minute 
tips for UraTIKAs? aTalent welcomes TIK students to visit their homebase at Autotalo on 
Tuesday 16.1. The idea is to take your job hunting tools and skills to the next level and 
offer you a possibility to ask whatever you like from recruiters! Throughout the evening 
there will be food, beverages and relaxed atmosphere to ensure a perfect venue to improve 
your job hunting skills!

During the night we’ll go through for example what is LinkedIn and how to get employers 
interested about you even without active effort, what does it take to impress recruiters 
with your CV and how to prepare yourself to UraTIKAs! Sign up to get your career boosted!

So that we can check and give hints for your LinkedIn profile and / or your CV, 
please bring your laptop and printed out CV!"""
  
  kilta.addSubtopic(sub1)
  
  kilta.addSubtopic(sub2)
  kilta.addSubtopic(sub3)
  
  def addSubtopicToTopic(topic: Topic, subtopic: Subtopic) = {
  	topic.addSubtopic(subtopic)
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