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
		
 		var str = "Viikko " + week + "\n"
		val thisWeek = this.subtopics.filter(s => s.date.day < day + 7)
		val nextWeek = this.subtopics.filter(s => s.date.day + 7 < day + 14)
		
		
		???
	}
}


object SubtopicOrdering extends Ordering[Subtopic] {
	def compare(a: Subtopic, b: Subtopic) = a.date compare b.date
}

class Subtopic(val name: String, val date: Date, val link: String = "") {
	var text = ""

}

