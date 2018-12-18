name := "MailGenerator"

version := "1.1"

scalaVersion := "2.12.7"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-swing
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"

mainClass in (Compile, run) := Some("gui.View")
mainClass in assembly := Some("gui.View")
