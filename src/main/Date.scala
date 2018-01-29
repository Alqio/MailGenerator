package main

import java.time.LocalDate

/**
 * A wrapper class for java.time.LocalDate
 * if the day is 30 and month 12, the date won't be displayed on text.
 */
class Date(val day: Int, val month: Int, val year:Int = 2018) extends Ordered[Date]{
	
  def compare(that: Date) = (this.day + this.month * 30) - (that.day + that.month * 30)
  
  def innerDate = LocalDate.of(2018, month, day)
  
  def isPast = innerDate.isBefore(LocalDate.now())
  
  override def toString = if (this.day == 30 && this.month == 12) "" else this.day + "." + this.month + "."
}
object Date {
	def apply(day: Int,month: Int) = new Date(day, month)
	
	def apply(str: String) = {
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