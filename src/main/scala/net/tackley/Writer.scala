package net.tackley

import java.io.File

import net.tackley.example.Event
import org.apache.avro.file.DataFileWriter
import org.apache.avro.specific.SpecificDatumWriter

object Writer extends App {

  val obj1 = Event(List("one", "two", "three"))
  val obj2 = Event(List("a", "b"))

  val datumWriter = new SpecificDatumWriter[Event](Event.SCHEMA$)
  val dataFileWriter = new DataFileWriter[Event](datumWriter)

  dataFileWriter.create(Event.SCHEMA$, new File("events.avro"))
  dataFileWriter.append(obj1)
  dataFileWriter.append(obj2)
  dataFileWriter.close()
}
