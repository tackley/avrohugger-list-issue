name := "Simple Project"

version := "1.0"

scalaVersion := "2.10.5"

sbtavrohugger.SbtAvrohugger.specificAvroSettings

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.4.1",
  "org.apache.avro" % "avro-mapred" % "1.7.7",
  "com.typesafe.play" %% "play-json" % "2.3.10"
)

