# MailGenerator

This is a software that I'm using for writing weekly letters to our organization's members.

###Features
* Produces a neatly categorized and consistent mail
* Organizes sub topics to topics
* Sorts sub topics by date
* Can save and load 
* Deletes past events on load
* Can create normal text files, html files that have style (bold, underscore & links) and special html files (drop down menus)
* Preview your mail while writing it

###Usage
Requirements: scala 2.12, sbt 1.2.4, OpenJDK 8. Might work on other versions too.

Compile:
```$xslt
sbt compile
```
Run:
```$xslt
sbt run
```
Assembly:
```$xslt
sbt assembly
```

For assembly, this project uses [sbt-assembly](https://github.com/sbt/sbt-assembly).

###Notes
* You can not edit sub topics. Instead, you have to select it, remove it and then edit it and the add it back.
* The software assumes that the weekly mail is written during monday and counts this week's and next week's events based on that.
* You can not edit topics inside the editor, you have to change the code if there is need for that (not very difficult though)
* For loading to work, you have to load a file that was saved as normal text file (not html or special html)
* This is designed to work on rather specific circumstances, some fixing might be needed to get this working to your usage.
* The software is designed to have maximum line length of 79.
