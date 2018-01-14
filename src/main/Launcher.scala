package main

object Launcher extends App{
  
	val mail = new Mail
	println(mail.generateTableOfContents)
	println(mail.generate)
	
}