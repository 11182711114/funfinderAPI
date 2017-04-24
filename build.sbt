import NativePackagerKeys._

herokuAppName in Compile := "morning-meadow-19114"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.7")
    sys.error("Java 7 is required for this project.")
}

name := """funfinder"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
