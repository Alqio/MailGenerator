package main

import collection.mutable.Map

/**
	* Class Mail represents the actual mail (surprise?). It consists of Topics which in turn consist of sub topics.
	*/
class Mail {

	val calendar = new Kalenteri()
	val guild = new Topic("Kilta")
	val ayy = new Topic("AYY & Aalto")
	val other = new Topic("Muut")
	val bottomCorner = new Topic("Pohjanurkkaus")

	var html = false
	var specialHtml = false

	var backupNumber = 1
	
	val topics = Vector(calendar, guild, ayy, other, bottomCorner)

	def addSubtopicToTopic(topic: Topic, subtopic: Subtopic): Unit = {
		topic.addSubtopic(subtopic)
		calendar.addSubtopic(subtopic)
	}

	def removeSubtopicFromTopic(topic: Topic, subtopic: Subtopic): Unit = {
		topic.subtopics.remove(topic.subtopics.indexOf(subtopic))
		calendar.subtopics.remove(calendar.subtopics.indexOf(subtopic))
	}

	def generateTableOfContents: String = {
		var str = "Sisällysluettelo\n"
		for (i <- 0 until topics.size) {
			str += topics(i).generateTableOfContents(i + 1)
		}
		str += "\n" + global.topicChangeMark
		str
	}
	
	def generateTableOfContentsHtml: String = {
		var str = "<h2>Sisällysluettelo</h2>\n"
		for (i <- 0 until topics.size) {
			str += topics(i).generateTableOfContentsHtml(i + 1)
		}
		str += "\n" + global.topicChangeMark
		str	  
	}

	def generateTableOfContentsSpecialHtml: String = {
		var str = "<h2>Sisällysluettelo</h2>\n"
		for (i <- 0 until topics.size) {
			str += topics(i).generateTableOfContentsSpecialHtml(i + 1)
		}
		str += "\n" + global.topicChangeMark
		str
	}

	def generate: String = {
		var str = ""
		for (i <- topics.indices) {
			str += topics(i).generate(i + 1)
		}
		str
	}

	def generateHtml: String = {
		var str = ""
		for (i <- topics.indices) {
			str += topics(i).generateHtml(i + 1)
		}
		str
	}
	
	def generateSpecialHtml: String = {
		var str = ""
		for (i <- topics.indices) {
			str += topics(i).generateHtmlSpecial(i + 1)
		}
		str		
	}
	
	def generateAll(forceNormal: Boolean = false): String = {
		if ((!html && !specialHtml) || forceNormal)
			this.generateTableOfContents + generate
		else {
			val head = """
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">    
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <base target="_parent">    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">  
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <style>
        body {
            font-family: monospace;
            border: 0px;
            margin: 0px;
            padding: 0px;
            display: block;
            font-size: 14px;
            line-height: 150%;
        }
        pre {
            white-space:pre-wrap;
            white-space: -moz-pre-wrap;
            white-space: -pre-wrap;
            white-space: -o-pre-wrap;
            word-wrap:break-word;
        }
    </style>

    <script>
       $(document).ready(function() {
            $(".btn").click(function () {
                $(this).find('i').toggleClass('fas fa-minus-circle');
            });
            $(".calendarSpecial").click(function() {
                let t = $(this);
                let name = t.attr("name");

                let el = $("#" + name);
                let parent = el.parent();

                let target = "#" + parent.attr('id');
                console.log(target);
                let el2 = $("button[data-target='" + target +"']");

                let is = parent.find('i');
                for (let i = 0; i < is.length; i++) {
                    let element = is[i];
                    console.log(element);
                    let p = $(element).parent();

                    if (p.attr("data-target") === "#" + name) {
                        $(element).toggleClass('fas fa-minus-circle');
                    }
                }
                el2.find('i').toggleClass('fas fa-minus-circle');
                el.collapse('toggle');
                parent.collapse('toggle');
           });
			 });
 
    </script>    

</head>


<body>
<div class="container">
<PRE>
"""
			
			val end = """
</PRE>
</div>
</body>
</html>
"""
			if (html)
				head + this.generateTableOfContentsHtml + generateHtml + end
			else
				head + this.generateTableOfContentsSpecialHtml + generateSpecialHtml + end
		}
	}
	

}

/**
	* A Mail singleton object that is used for loading mails and then creating Mail instances based on that
	*/
object Mail {

	/**
	 * Generate and fill an mail instance from an array of strings (lines of the file).
	 * @param lines the lines that were read from file
	 * @ret the mail instance
	 */
	def createFromString(lines: Array[String]): Mail = {
		
		val mail = new Mail
		val signups = Map[String, (Date, Date)]()
		var text = lines.clone
		
		//Drop table of contents
		text = text.dropWhile(_ != "----")
		
		//Drop until we are at Calendar and then drop to signups.
		text = text.dropWhile(_ != "1. Kalenteri").drop(2).dropWhile(_ != "").drop(2)
		
		println("TEXT(0): " + text(0))
		
		
		//Read in signups that will be added last to subtopics after they have been created
		var i = 0
		while (text(i) != "") {
			var row = text(i).dropWhile(_ == ' ')
			val start = row.takeWhile(_ != ' ')
			row = row.drop(start.size).dropWhile(!_.isDigit)
			val end = row.takeWhile(_ != ' ')
			row = row.drop(end.size).dropWhile(_ == ' ')
			val name = row.trim
			
			signups += name -> (Date(start), Date(end))
			
			i += 1
		}
		text = text.drop(i)
		
		//Drop until we are the first subtopic in section 2. Kilta
		text = text.dropWhile(_ != "2. Kilta").drop(2)

		move(mail.guild)
		move(mail.ayy)
		move(mail.other)
		move(mail.bottomCorner)

		def move(topic: Topic): Unit = {
			try {
				do {
					addSubtopic(topic)
				} while (text.size > 0 && text(1) == "---")
				
				//Now text(1) should be "----", because it's time to change topics
				text = text.dropWhile(row => row == "").drop(3).dropWhile(row => row == "")
				println(text.take(3).mkString("\n"))
			} catch {
				case ex: Exception => println("Error when creating mail: " + ex.getMessage())
			}
		}

		def addSubtopic(topic: Topic): Unit = {
			if (text(1) == "---") {
				text = text.drop(2)
			}
			
			val row = text(0).reverse
			
			val d = if (row(0) == '.') row.takeWhile(_ != ' ').reverse else ""
			
			val name = text(0).dropWhile(_ != ' ').dropRight(d.size).trim

			val datestr = d//text(0).dropWhile(_ != ' ').drop(name.length + 1).trim

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

		}

		//Add signup dates for signups now that they have been created
		for (signup <- signups) {
			val name = signup._1
			val date = signup._2

			for (subtopic <- mail.guild.subtopics ++ mail.ayy.subtopics ++ mail.other.subtopics) {
				if (subtopic.name == name) {
					subtopic.signup_start = date._1
					subtopic.signup_end = date._2
				}
			}
			
		}
		mail
	}

}
