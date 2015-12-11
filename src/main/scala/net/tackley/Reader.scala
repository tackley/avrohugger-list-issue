package net.tackley

import java.io.File

import net.tackley.example.Event
import org.apache.avro.file.DataFileReader
import org.apache.avro.specific.SpecificDatumReader

object Reader extends App {

  val datumReader = new SpecificDatumReader[Event](Event.SCHEMA$)
  val fileReader = new DataFileReader[Event](new File("events.avro"), datumReader)

  // This isn't great scala, but represents how org.apache.avro.mapred.AvroInputFormat
  // (via org.apache.avro.file.DataFileStream) interacts with the SpecificDatumReader.

  var event: Event = _

  while (fileReader.hasNext) {
    event = fileReader.next(event)
    println(event)
  }

  fileReader.close()

}
