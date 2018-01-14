package main

class Date(val day: Int, val month: Int, val year:Int = 2018) extends Ordered[Date]{
  def compare(that: Date) = this.day - that.day
}