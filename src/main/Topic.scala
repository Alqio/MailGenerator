package main

import scala.collection.mutable.Buffer
import java.util.Calendar
import java.time._

class Topic(val name: String) {
	val subtopics = Buffer[Subtopic]()

	def addSubtopic(subtopic: Subtopic) = {
		this.subtopics += subtopic
	}

	/**
	 * Order subtopics by date, ascending
	 */
	def sortSubtopics = {
		val sorted = subtopics.toArray
		util.Sorting.quickSort(sorted)(SubtopicOrdering)
		sorted
	}
	/**
	 * Order subtopics by date, ascending
	 */
	def sortSubtopicsBySignup = {
		val sorted = subtopics.toArray
		util.Sorting.quickSort(sorted)(SubtopicOrdering2)
		sorted
	}

	def generateTableOfContents(number: Int) = {
		var str = number + ". " + name + "\n"
		val sorted = sortSubtopics
		for (i <- 0 until subtopics.size) {
			str += "  " + (number + "." + (i + 1) + " " + sorted(i).name) + "\n"
		}
		str
	}

	def generateHtml(number: Int) = {
		println("Generating html for number " + number)
		var str = "\n<button type=\"button\" class=\"btn\" data-toggle=\"collapse\" data-target=\"#" + name.replace(' ', '_').replace('&', 'U') + 
							"\"><h2><i class=\"fa fa-plus-circle\" style=\"font-size:24px\"></i>  "+ number + ". " + name + "</h2></button>\n"
		
		//val s = "<div id=\"" + "\" class=\"collapse\">
		str += "<div id=\"" + name.replace(' ', '_').replace('&', 'U') + "\" class=\"collapse\">\n\n"
		
		val sorted = sortSubtopics

		for (i <- 0 until sorted.size) {
			
			val realName = sorted(i).name.replace(' ', '_').map(x => if (x.isLetter) x else "ZZ").mkString
			println(realName)
			
			str += "\n<button type=\"button\" class=\"btn\" data-toggle=\"collapse\" data-target=\"#" + realName +
						 "\"><i class=\"fa fa-plus-circle\" style=\"font-size:16px\"></i>  "+ number + "."+ (i + 1) + " " + sorted(i).name + 
						 (if (sorted(i).displayDate) " " + sorted(i).date else "") + "</u></button>\n"
			
			str += "<div id=\"" + realName + "\" class=\"collapse\">\n\n"
			
			val textAsArray = sorted(i).text.split('\n')
			val textFixedArray = Buffer[String]()
			
			for (line <- textAsArray) {
				val splitted = line.split(' ')
				println(splitted)
				val beforeLink = splitted.takeWhile(word => !word.contains("http"))
				println("before link: " + beforeLink.mkString(", "))
				
				if (beforeLink.size < splitted.size) {
					val link = splitted(beforeLink.size)
					println(link)
					if (beforeLink.size == 0) {
						splitted(beforeLink.size) = "\n\n<a href=\"" + link + "\">" + link + "</a>"
					} else {
						splitted(beforeLink.size) = "<a href=\"" + link + "\">" + link + "</a>"
					}
					
				}
				textFixedArray += splitted.mkString(" ")
				
//				if (line.startsWith("http")) {
//					val link = line.takeWhile(c => c != ' ' && c != '\n')
//					val l = "\n\n<a href=\"" + link + "\">" + link + "</a>"
//					textFixedArray += l
//				} else {
//					textFixedArray += line
//				}
			}
			
			
			str += "<p>" + textFixedArray.mkString("\n") + "</p>"
			
			//str += "<u>" + number + "." + (i + 1) + " " + sorted(i).name + (if (sorted(i).displayDate) " " + sorted(i).date else "") + "</u>\n<p>" + sorted(i).text + "</p>"
			
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
	
	def generate(number: Int) = {
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

	override def toString = this.name
	
}

class Kalenteri() extends Topic("Kalenteri") {
  
  override def generateTableOfContents(n: Int) = n + ". " + this.name + "\n"
  
	override def generate(number: Int): String = {
		
		val now = Calendar.getInstance()
		val week = now.get(Calendar.WEEK_OF_YEAR)
		val day = now.get(Calendar.DAY_OF_MONTH)
		
 		var str = "\n\n" + number + ". " + this.name + "\n\nTällä viikolla\n"
		
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
		
		val signups = sorted2.filter(s => s.signup_start.innerDate.isBefore(nextWeek) && s.signup_end.innerDate.isAfter(date))
		
		for (subtopic <- signups) {
			str += "  " + subtopic.signup_start + " - " + subtopic.signup_end + " " + subtopic.name + "\n"
		}
		str += "\n"
		str + "----\n"
		
	}
	override def generateHtml(number: Int): String = {
		
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
		val signups = sorted.filter(s => s.signup_start.innerDate.isBefore(nextWeek) && s.signup_end.innerDate.isAfter(date))
		
		for (subtopic <- signups) {
			str += "  " + subtopic.signup_start + " - " + subtopic.signup_end + " " + subtopic.name + "\n"
		}
		str += "\n"
		str + "----\n"
		
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
	
	override def toString = name
	
	def this(name: String, date: Date, loaded: Boolean) = {
		this(name, date, "", loaded)
	}

}

