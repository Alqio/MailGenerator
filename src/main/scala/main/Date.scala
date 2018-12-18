package main

import java.time.LocalDate

/**
  * A wrapper class for java.time.LocalDate
  * if the day is 30 and month 12, the date won't be displayed on text.
  */
class Date(val day: Int, val month: Int, val year: Int = global.year) extends Ordered[Date] {

  def compare(that: Date): Int = (this.day + this.month * 30) - (that.day + that.month * 30)

  def innerDate: LocalDate = LocalDate.of(global.year, month, day)

  def isPast: Boolean = innerDate.isBefore(LocalDate.now())

  def ==(another: Date): Boolean = {
    this.day == another.day && this.month == another.month && this.year == another.year
  }

  override def toString: String = this.day + "." + this.month + "."
}

object Date {
  def apply(day: Int, month: Int) = new Date(day, month)

  def apply(str: String): Date = {
    try {
      val day = str.takeWhile(_ != '.').toInt
      val month = str.drop(day.toString.size + 1).dropRight(1).toInt
      new Date(day, month)
    } catch {
      case ex: Exception => {
        println("Error when parsing date. " + ex.getMessage())
        new Date(30, 12)
      }
    }
  }

}