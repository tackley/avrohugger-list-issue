name := "Simple Project"

version := "1.0"

scalaVersion := "2.10.5"

sbtavrohugger.SbtAvrohugger.specificAvroSettings

libraryDependencies ++= Seq(
  "org.apache.avro" % "avro-mapred" % "1.7.7"
)

