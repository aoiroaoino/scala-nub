organization := "com.github.aoiroaoino"

name := "Scala.nub"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions in Test ++= Seq(
//  "-Xshow-phases",
  "-Xplugin:target/scala-2.11/scala-nub_2.11-0.1.0-SNAPSHOT.jar"
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % "2.11.8"
)
