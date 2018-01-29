package main

class Mail {

	val kalenteri = new Kalenteri()
	val kilta = new Topic("Kilta")
	val ayy = new Topic("AYY & Muut")
	val pohjanurkkaus = new Topic("Pohjanurkkaus")


	val topics = Vector(kalenteri, kilta, ayy, pohjanurkkaus)

	def addSubtopicToTopic(topic: Topic, subtopic: Subtopic) = {
		topic.addSubtopic(subtopic)
		kalenteri.addSubtopic(subtopic)
	}
	def removeSubtopicFromTopic(topic: Topic, subtopic: Subtopic) = {
		topic.subtopics.remove(topic.subtopics.indexOf(subtopic))
		kalenteri.subtopics.remove(kalenteri.subtopics.indexOf(subtopic))
	}

	def generateTableOfContents = {
		var str = "Sisällysluettelo\n"
		for (i <- 0 until topics.size) {
			str += topics(i).generateTableOfContents(i + 1)
		}
		str += "\n" + global.topicChangeMark
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

		move(mail.kilta)
		move(mail.ayy)
		move(mail.pohjanurkkaus)

		def move(topic: Topic) = {
			try {
				do {
					addSubtopic(topic)
				} while (text.size > 0 && text(1) == "---")
				
				//Eli nyt text(1) pitäisi olla "----", koska on aiheenvaihdon aika
				text = text.dropWhile(row => row == "").drop(3).dropWhile(row => row == "")
				println(text.take(3).mkString("\n"))
			} catch {
				case ex: Exception => println("Error when creating mail: " + ex.getMessage())
			}
		}

		def addSubtopic(topic: Topic) = {
			if (text(1) == "---") {
				text = text.drop(2)
			}
			val name = text(0).dropWhile(_ != ' ').drop(1).takeWhile(!_.isDigit).trim

			val datestr = text(0).dropWhile(_ != ' ').drop(name.length + 1).trim

			val date = try {
				Date(datestr)
			} catch {
				case ex: Exception => Date(30, 12)
			}
			//text = text.drop(1)
			//drop(1) so the name is not included
			val subtopicText = text.drop(1).takeWhile(row => row != "---" && row != "----")
			text = text.drop(subtopicText.length)

			val subtopic = new Subtopic(name, date, true)
			subtopic.text = subtopicText.mkString("\n")

			if (!subtopic.date.isPast) mail.addSubtopicToTopic(topic, subtopic)

			//println(subtopic.text)
			//println(topic.generate(2))
		}

		println("@@@@@@@@@@")
		//println(text.mkString("\n"))
		//println(mail.generateAll)
		mail
	}

}