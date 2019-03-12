package main

import scala.collection.mutable.Buffer
import java.util.Calendar
import java.time._

class Topic(val name: String) {
	val subtopics = Buffer[Subtopic]()

	def addSubtopic(subtopic: Subtopic): Unit = {
		this.subtopics += subtopic
	}

	/**
	 * Order subtopics by date, ascending
	 */
	def sortSubtopics: Array[Subtopic] = {
		val sorted = subtopics.toArray
		util.Sorting.quickSort(sorted)(SubtopicOrdering)
		sorted
	}
	/**
	 * Order subtopics by signup date, ascending
	 */
	def sortSubtopicsBySignup: Array[Subtopic] = {
		val sorted = subtopics.toArray
		util.Sorting.quickSort(sorted)(SubtopicOrdering2)
		sorted
	}

	def generateTableOfContents(number: Int): String = {
		var str = number + ". " + name + "\n"
		val sorted = sortSubtopics
		for (i <- 0 until subtopics.size) {
			val subtopic = sorted(i)
			str += "  " + (number + "." + (i + 1) + " " + sorted(i).name) + "\n"
		}
		str
	}

	def generateTableOfContentsHtml(number: Int): String = {
		var str = number + ". " + name + "\n"
		val sorted = sortSubtopics
		for (i <- 0 until subtopics.size) {
			val subtopic = sorted(i)
			str += "  " + "<a href=\"#" + subtopic.slug + "\" class=\"calendar\" name=\"" + subtopic.slug + "\">" + (number + "." + (i + 1) + " " + sorted(i).name) + "</a>\n"
		}
		str
	}

	def generateTableOfContentsSpecialHtml(number: Int): String = {
		var str = number + ". " + name + "\n"
		val sorted = sortSubtopics
		for (i <- 0 until subtopics.size) {
			val subtopic = sorted(i)
			str += "  " + "<a href=\"#" + subtopic.slug + "\" class=\"calendarSpecial\" name=\"" + subtopic.slug + "\">" + (number + "." + (i + 1) + " " + sorted(i).name) + "</a>\n"
		}
		str
	}

	/**
		* Generates the HTML version of the Topic's contents
		* @param number
		* @return String
		*/
	def generateHtml(number: Int): String = {
		var str = "\n<h2>" + number + ". " + name + "</h2>\n\n"
		val sorted = sortSubtopics

		for (i <- 0 until sorted.size) {
			str += "<u>" + number + "." + (i + 1) + " " + sorted(i).name + (if (sorted(i).displayDate) " " + sorted(i).date else "") + "</u>\n"

			val textAsArray = sorted(i).text.split('\n')
			val textFixedArray = Buffer[String]()
			
			for (line <- textAsArray) {
				val splitted = line.split(' ')

				val beforeLink = splitted.takeWhile(word => !word.contains("http"))

				
				if (beforeLink.size < splitted.size) {
					val link = splitted(beforeLink.size)

					if (beforeLink.size == 0) {
						splitted(beforeLink.size) = "\n\n<a href=\"" + link + "\">" + link + "</a>"
					} else {
						splitted(beforeLink.size) = "<a href=\"" + link + "\">" + link + "</a>"
					}
					
				}
				textFixedArray += splitted.mkString(" ")
				

			}
			
			
			str += "<p>" + textFixedArray.mkString("\n") + "</p>"
			
			if (sorted(i).link != "") str += "\n\n" + "<a href=\"" + sorted(i).link + "\">" + sorted(i).link + "</a>"
			
			
			if (sorted(i).loaded) {
				if (i < sorted.size - 1) str += "\n" + global.subtopicChangeMark + "\n" else str += "\n"
			} else {
				if (i < sorted.size - 1) str += "\n\n\n" + global.subtopicChangeMark + "\n" else str += "\n\n"
				
			}
			
		}
		str += global.topicChangeMark + "\n"
		str		
	}


	/**
		* Generates the Special HTML with drop down lists of the Topic's contents
		* @param number
		* @return
		*/
	def generateHtmlSpecial(number: Int): String = {

		var str = "\n<button type=\"button\" class=\"btn\" data-toggle=\"collapse\" data-target=\"#" + name.replace(' ', '_').replace('&', 'U') + 
							"\"><h2><i class=\"fa fa-plus-circle\" style=\"font-size:24px\"></i>  "+ number + ". " + name + "</h2></button>\n"
		
		//val s = "<div id=\"" + "\" class=\"collapse\">
		str += "<div id=\"" + name.replace(' ', '_').replace('&', 'U') + "\" class=\"collapse\">\n\n"
		
		val sorted = sortSubtopics

		for (i <- 0 until sorted.size) {
			
			val realName = sorted(i).slug
			//val realName = sorted(i).name.replace(' ', '_').map(x => if (x.isLetter) x else "ZZ").mkString
			
			str += "\n<button type=\"button\" class=\"btn\" data-toggle=\"collapse\" data-target=\"#" + realName +
						 "\"><i class=\"fa fa-plus-circle\" style=\"font-size:16px\"></i>  "+ number + "."+ (i + 1) + " " + sorted(i).name + 
						 (if (sorted(i).displayDate) " " + sorted(i).date else "") + "</u></button>\n"
			
			str += "<div id=\"" + realName + "\" class=\"collapse\">\n\n"
			
			val textAsArray = sorted(i).text.split('\n')
			val textFixedArray = Buffer[String]()
			
			for (line <- textAsArray) {
				val splitted = line.split(' ')

				val beforeLink = splitted.takeWhile(word => !word.contains("http"))

				
				if (beforeLink.size < splitted.size) {
					val link = splitted(beforeLink.size)

					if (beforeLink.size == 0) {
						splitted(beforeLink.size) = "\n\n<a href=\"" + link + "\">" + link + "</a>"
					} else {
						splitted(beforeLink.size) = "<a href=\"" + link + "\">" + link + "</a>"
					}
					
				}
				textFixedArray += splitted.mkString(" ")

			}
			
			
			str += "<p>" + textFixedArray.mkString("\n") + "</p>"
			
			if (sorted(i).link != "") str += "\n\n" + "<a href=\"" + sorted(i).link + "\">" + sorted(i).link + "</a>"
			

			if (sorted(i).loaded) {
				if (i < sorted.size - 1) str += "\n" + global.subtopicChangeMark + "\n" else str += "\n"
			} else {
				if (i < sorted.size - 1) str += "\n\n\n" + global.subtopicChangeMark + "\n" else str += "\n\n"
				
			}
			str += "</div>"
			
		}
		str += "</div>"
		str += global.topicChangeMark + "\n"
		str		
	}

	/**
		* Generates the normal text of this Topic's contents
		* @param number
		* @return
		*/
	def generate(number: Int): String = {
		var str = "\n" + number + ". " + name + "\n\n"
		val sorted = sortSubtopics

		for (i <- 0 until sorted.size) {
			
			str += number + "." + (i + 1) + " " + sorted(i).name + (if (sorted(i).displayDate) " " + sorted(i).date else "") + "\n" + sorted(i).text
			
			if (sorted(i).link != "") str += "\n\n" + sorted(i).link
			
			
			if (sorted(i).loaded) {
				if (i < sorted.size - 1) str += "\n" + global.subtopicChangeMark + "\n" else str += "\n"
			} else {
				if (i < sorted.size - 1) str += "\n\n\n" + global.subtopicChangeMark + "\n" else str += "\n\n"
				
			}
			
		}
		str += global.topicChangeMark + "\n"
		str
	}

	override def toString: String = this.name
	
}

class Kalenteri() extends Topic("Kalenteri") {
	
	override def sortSubtopicsBySignup: Array[Subtopic] = {
		val sorted = subtopics.toArray
		util.Sorting.quickSort(sorted)(SubtopicOrdering2)
		sorted
	}  
  override def generateTableOfContents(n: Int): String = n + ". " + this.name + "\n"

	override def generateTableOfContentsHtml(number: Int): String = this.generateTableOfContents(number)

	override def generateTableOfContentsSpecialHtml(number: Int): String = this.generateTableOfContents(number)

	override def generate(number: Int): String = {
		
		val now = Calendar.getInstance()
		val week = now.get(Calendar.WEEK_OF_YEAR)
		val day = now.get(Calendar.DAY_OF_MONTH)
		
 		var str = "\n\n" + number + ". " + this.name + "\n\nTällä viikolla\n"
		
		val date = LocalDate.now()
		val nextWeek = date.plusWeeks(1)
		val sorted = sortSubtopics
		val thisweek = sorted.filter(s => s.date.innerDate.isBefore(nextWeek))
		val nextweek = sorted.filter(s => (s.date.innerDate.isBefore(nextWeek.plusWeeks(1)) && s.date.innerDate.isAfter(nextWeek)) || s.date.innerDate.isEqual(nextWeek))
		
		for (i <- 0 until thisweek.size) {
			str += "  " + thisweek(i).date + " " + thisweek(i).name + "\n"
		}
		str += "Ensi viikolla\n"
		for (i <- 0 until nextweek.size) {
			str += "  " + nextweek(i).date + " " + nextweek(i).name + "\n"
		}
		
		str += "\n"
		
		//Signups
		str += "Tällä viikolla auki olevat ilmoittautumiset\n"
		
		val sorted2 = sortSubtopicsBySignup
		
		val signups = sorted2.filter(s => s.signup_start.innerDate.isBefore(nextWeek) && s.signup_end.innerDate.isAfter(date))
		
		for (subtopic <- signups) {
			str += "  " + subtopic.signup_start + " - " + subtopic.signup_end + " " + subtopic.name + "\n"
		}
		str += "\n"
		str + "----\n"
		
	}
	override def generateHtmlSpecial(number: Int): String = {
		
		val now = Calendar.getInstance()
		val week = now.get(Calendar.WEEK_OF_YEAR)
		val day = now.get(Calendar.DAY_OF_MONTH)

		var str = "\n\n<h2>" + number + ". " + this.name + "</h2>\n\nTällä viikolla\n"

		val date = LocalDate.now()

		val nextWeek = date.plusWeeks(1)
		val sorted = sortSubtopics
		val thisweek = sorted.filter(s => s.date.innerDate.isBefore(nextWeek))
		val nextweek = sorted.filter(s => s.date.innerDate.isBefore(nextWeek.plusWeeks(1)) && s.date.innerDate.isAfter(nextWeek))
		
		for (i <- 0 until thisweek.size) {
			str += "  " + thisweek(i).date + " " + thisweek(i).name + "\n"
		}
		str += "Ensi viikolla\n"
		for (i <- 0 until nextweek.size) {
			str += "  " + nextweek(i).date + " " + nextweek(i).name + "\n"
		}
		
		str += "\n"
		
		//Signups
		str += "Tällä viikolla auki olevat ilmoittautumiset\n"
		
		val sorted2 = sortSubtopicsBySignup
		
		val signups = sorted.filter(s => s.signup_start.innerDate.isBefore(nextWeek) && s.signup_end.innerDate.isAfter(date))
		
		for (subtopic <- signups) {
			str += "  " + subtopic.signup_start + " - " + subtopic.signup_end + " " + subtopic.name + "\n"
			val t = subtopic.signup_start + " - " + subtopic.signup_end + " " + subtopic.name
			//str += "  <a href=\"#" + subtopic.slug + "\" class=\"calendarSpecial\" name=\"" + subtopic.slug + "\">" + t + "</a>\n"
			//<a href="#aiheZZ" class="calendar" id="aihe1" name="aiheZZ">23.2. aihe1</a>
		}
		str += "\n"
		str + "----\n"
		
	}
	override def generateHtml(number: Int): String = {
		this.generateHtmlSpecial(number)
	}
  
}


object SubtopicOrdering extends Ordering[Subtopic] {
	def compare(a: Subtopic, b: Subtopic) = a.date compare b.date
}

object SubtopicOrdering2 extends Ordering[Subtopic] {
	def compare(a: Subtopic, b: Subtopic) = a.signup_end compare b.signup_end
}

class Subtopic(val name: String, val date: Date, val link: String = "", val loaded: Boolean = false) {
	var text = ""
	var displayDate = true
	
	var signup_start = Date("30.12.")
	var signup_end = Date("30.12.")
	
	if (date == Date("30.12.") || date == Date("31.12.")) {
		displayDate = false
	}
	
	override def toString: String = name


	def slug: String = name.replace(' ', '_').map(x => if (x.isLetter || x.isDigit) x else "ZZ").mkString

	def this(name: String, date: Date, loaded: Boolean) = {
		this(name, date, "", loaded)
	}

}

