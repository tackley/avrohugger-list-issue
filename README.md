# avrohugger-list-issue
Illustrates https://github.com/julianpeeters/avrohugger/issues/25

With the code checked out as is, the following behaviour is observed:

```
$ sbt run
[info] Loading global plugins from /Users/gtackley/.sbt/0.13/plugins
[info] Loading project definition from /Users/gtackley/working/avrohugger-list-demo/project
[info] Set current project to Simple Project (in build file:/Users/gtackley/working/avrohugger-list-demo/)
[warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list

Multiple main classes detected, select one to run:

 [1] net.tackley.Reader
 [2] net.tackley.Writer

Enter number: 1

[info] Running net.tackley.Reader
{"someArray": ["one", "two", "three"]}
[error] (run-main-0) java.lang.UnsupportedOperationException
java.lang.UnsupportedOperationException
	at java.util.AbstractList.remove(AbstractList.java:161)
	at java.util.AbstractList$Itr.remove(AbstractList.java:374)
	at java.util.AbstractList.removeRange(AbstractList.java:571)
	at java.util.AbstractList.clear(AbstractList.java:234)
	at org.apache.avro.generic.GenericDatumReader.newArray(GenericDatumReader.java:330)
	at org.apache.avro.generic.GenericDatumReader.readArray(GenericDatumReader.java:216)
	at org.apache.avro.generic.GenericDatumReader.read(GenericDatumReader.java:153)
	at org.apache.avro.generic.GenericDatumReader.readField(GenericDatumReader.java:193)
	at org.apache.avro.generic.GenericDatumReader.readRecord(GenericDatumReader.java:183)
	at org.apache.avro.generic.GenericDatumReader.read(GenericDatumReader.java:151)
	at org.apache.avro.generic.GenericDatumReader.read(GenericDatumReader.java:142)
	at org.apache.avro.file.DataFileStream.next(DataFileStream.java:233)
	at net.tackley.Reader$delayedInit$body.apply(Reader.scala:20)
	at scala.Function0$class.apply$mcV$sp(Function0.scala:40)
	at scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:12)
	at scala.App$$anonfun$main$1.apply(App.scala:71)
	at scala.App$$anonfun$main$1.apply(App.scala:71)
	at scala.collection.immutable.List.foreach(List.scala:318)
	at scala.collection.generic.TraversableForwarder$class.foreach(TraversableForwarder.scala:32)
	at scala.App$class.main(App.scala:71)
	at net.tackley.Reader$.main(Reader.scala:9)
	at net.tackley.Reader.main(Reader.scala)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:483)
```

If I manually edit the generated code from:

```scala
 def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => {
        java.util.Arrays.asList(({
          someArray map { x =>
            x
          }
        }: _*))
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.someArray = {
        value match {
          case (array: org.apache.avro.generic.GenericData.Array[_]) => {
            scala.collection.JavaConversions.asScalaIterator(array.iterator).toList map { x =>
              x.toString
            }
          }
        }
      }.asInstanceOf[List[String]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }

```

to 

```scala
  def get(field: Int): AnyRef = {
    field match {
      case pos if pos == 0 => {
        scala.collection.JavaConversions.bufferAsJavaList(({
          someArray map { x =>
            x
          }
        }.toBuffer))
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field: Int, value: Any): Unit = {
    field match {
      case pos if pos == 0 => this.someArray = {
        value match {
          case (array: java.util.List[_]) => {
            scala.collection.JavaConversions.asScalaIterator(array.iterator).toList map { x =>
              x.toString
            }
          }
        }
      }.asInstanceOf[List[String]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }

```

(note: I need to make the match more generic on the put, too)

Then the expected behaviour is observed:

```
Multiple main classes detected, select one to run:

 [1] net.tackley.Reader
 [2] net.tackley.Writer

Enter number: 1

[info] Running net.tackley.Reader
{"someArray": ["one", "two", "three"]}
{"someArray": ["a", "b"]}
```



