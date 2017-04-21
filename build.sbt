import NativePackagerKeys._

herokuAppName in Compile := "morning-meadow-19114"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"

name := """funfinder"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
