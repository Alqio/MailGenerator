package main

class Date(val day: Int, val month: Int, val year:Int = 2018) extends Ordered[Date]{
  def compare(that: Date) = this.day - that.day
  
  override def toString = this.day + "." + this.month + "."
}
object Date {
	def apply(day: Int,month: Int) = new Date(day, month)
	
	def apply(str: String) = {
		val day = str.takeWhile(_ != '.').toInt
		val month = str.drop(day.toString.size + 1).dropRight(1).toInt
		new Date(day, month)
	}
	
}