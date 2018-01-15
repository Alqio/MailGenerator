package main

import scala.collection.mutable.Buffer
import java.util.Calendar

class Topic(val name: String) {
	val subtopics = Buffer[Subtopic]()

	val subtopicChangeMark = "---"

	def addSubtopic(subtopic: Subtopic) = {
		this.subtopics += subtopic
	}

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

	def generate(number: Int) = {
		var str = "\n" + number + ". " + name + "\n\n"
		val sorted = sortSubtopics

		for (i <- 0 until sorted.size) {
			str += number + "." + (i + 1) + " " + sorted(i).name + " " + sorted(i).date + "\n" + sorted(i).text
			
			if (sorted(i).link != "") str += "\n\n" + sorted(i).link
			
			if (i < sorted.size - 1) str += "\n\n\n" + subtopicChangeMark + "\n" else str += "\n\n"
		}
		str += "----\n"
		str
	}

	override def toString = this.name
	
}

class Kalenteri() extends Topic("Kalenteri") {
	override def generate(number: Int): String = {
		
		val now = Calendar.getInstance()
		val week = now.get(Calendar.WEEK_OF_YEAR)
		val day = now.get(Calendar.DAY_OF_MONTH)
		
 		var str = "\nTämä viikko\n"
		import java.time._
		val date = LocalDate.now()
		val nextWeek = date.plusWeeks(1)
		val sorted = sortSubtopics
		val thisweek = sorted.filter(s => s.date.innerDate.isBefore(nextWeek))
		val nextweek = sorted.filter(s => s.date.innerDate.isBefore(nextWeek.plusWeeks(1)) && s.date.innerDate.isAfter(nextWeek))
		
		for (i <- 0 until thisweek.size) {
			str += "  " + thisweek(i).name + "\n"
		}
		str += "Ensi viikko\n"
		for (i <- 0 until nextweek.size) {
			str += "  " + nextweek(i).name + "\n"
		}
		str + "\n"
	}
}


object SubtopicOrdering extends Ordering[Subtopic] {
	def compare(a: Subtopic, b: Subtopic) = a.date compare b.date
}

class Subtopic(val name: String, val date: Date, val link: String = "") {
	var text = ""

}

