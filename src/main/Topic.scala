package main

import scala.collection.mutable.Buffer

class Topic(val name: String) {
  val subtopics = Buffer[Subtopic]()
  val number = 1
  
  def addSubtopic(subtopic: Subtopic) = {
  	this.subtopics += subtopic
  }
  
  def generate() = {
  	var str = ""
  	for (i <- 0 until subtopics.size) {
  		str += number + "." + (i+1) + " " + subtopics(i).name
  		
  	}
  }
  
}

class Subtopic(val name: String, val date: Date) {
	val link = ""
	var text = ""
}

