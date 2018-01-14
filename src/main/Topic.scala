package main

import scala.collection.mutable.Buffer

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

object SubtopicOrdering extends Ordering[Subtopic] {
	def compare(a: Subtopic, b: Subtopic) = a.date compare b.date
}

class Subtopic(val name: String, val date: Date, val link: String = "") {
	var text = ""

}

