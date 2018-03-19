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

	def generateTableOfContents(number: Int) = {
		var str = number + ". " + name + "\n"
		val sorted = sortSubtopics
		for (i <- 0 until subtopics.size) {
			str += "  " + (number + "." + (i + 1) + " " + sorted(i).name) + "\n"
		}
		str
	}

	def generateHtml(number: Int) = {
		var str = "\n<h2>" + number + ". " + name + "</h2>\n\n"
		val sorted = sortSubtopics

		for (i <- 0 until sorted.size) {
			str += "<u>" + number + "." + (i + 1) + " " + sorted(i).name + " " + sorted(i).date + "</u>\n<p>" + sorted(i).text + "</p>"
			
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
		val signups = sorted.filter(s => s.signup_start.innerDate.isBefore(nextWeek) && s.signup_end.innerDate.isAfter(date))
		
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

