package main

import scala.io.Source

/**
  * TODO: Load these from config file
  */
object global {
  //default values
  var topicChangeMark = "----"
  var subtopicChangeMark = "---"
  var year = 2019
  var maxBackups = 3

  //loadConfiguration()

  def loadConfiguration() = {

    val configs = collection.mutable.Map[String, String]()

    val bufferedSource = Source.fromFile("settings.cfg")
    for (line <- bufferedSource.getLines) {
      val attr = line.takeWhile(_ != '=').trim
      val value = line.dropWhile(_ != '=').drop(1).trim
      configs += attr -> value
    }

    bufferedSource.close

    topicChangeMark = configs("topicChangeMark")
    subtopicChangeMark = configs("subtopicChangeMark")
    year = configs("year").asInstanceOf[Int]
    maxBackups = configs("maxBackups").asInstanceOf[Int]
    println("Loaded settings")
  }

}

