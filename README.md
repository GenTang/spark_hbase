spark_hbase
===========

Spark has their own example about integrating HBase and Spark in scala [HBaseTest.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/HBaseTest.scala) and python converter [HBaseConverters.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/pythonconverters/HBaseConverters.scala). 

However, the python converter `HBaseResultToStringConverter` in  [HBaseConverters.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/pythonconverters/HBaseConverters.scala) return only the value of first column in the result. And [HBaseTest.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/HBaseTest.scala) stops just at returning *org.apache.hadoop.hbase.client.Result* and doing count() call.

An example in Scala of reading data saved in hbase by Spark and an example of converter for python

The example in scala [HBaseInput.scala](/src/main/scala/examples/HBaseInput.scala) transfers the data saved in hbase into `RDD[String]` which contains *row, column:cell, timestamp, value, type*. 

The example of converter for python [pythonConverters.scala](/src/main/scala/examples//pythonConverters.scala) transfer the data saved in hbase into string which contains the same information as the example above. We can use `ast` package to easily transfer this string to dictionary

How to run
=========
1. Make sure that you well set up [git](https://help.github.com/articles/set-up-git/#platform-linux)
2. Download this application by 

  ```bash
   $ git clone https://github.com/GenTang/spark_hbase.git
  ```

3. Build the assembly by using SBT `assembly`

  ```bash
  $ <the path to spark_hbase>/sbt/sbt clean assembly
  ```

* Run example python script [hbase_input.py](hbase_input.py) which use pythonConverter `ImmutableBytesWritableToStringConverter` and `HBaseResultToStringConverter` to convert the data in hbase to dictionary

  * If you are using `SPARK_CLASSPATH`:
     1. Add `export SPARK_CLASSPATH=$SPARK_CLASSPATH":<the path to hbase>/lib/*:<the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar` to `./conf/spark-env.sh`.
  
    2. Launch the script by 
      ```bash
      $ ./bin/spark-submit <the path to hbase_input.py> \
         <host> <table> <column>
      ```

  * You can also use `spark.executor.extraClassPath` and `--driver-class-path` (recommended):
     1. Add `spark.executor.extraClassPath <the path to hbase>/lib/*` to `spark-defaults.conf`.

     2. Launch the script by
       ```bash
        $ ./bin/spark-submit \
           --driver-class-path <the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar \
           <the path to hbase_input.py> \
           <host> <table> <column>
       ```

* Run example scala script [HBaseInput.scala](/src/main/scala/examples/HBaseInput.scala)
  * If you are using `SPARK_CLASSPATH`:
     1. Add `export SPARK_CLASSPATH=$SPARK_CLASSPATH":<the path to hbase>/lib/*` to `./conf/spark-env.sh`.

     2. Launch the script by 
      ```bash
      $ ./bin/spark-submit \
         --class examples.HBaseInput \
         <the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar \
         <host> <table> 
      ```
      
  * You can also use `spark.executor.extraClassPath` and `--driver-class-path` (recommended):
     1. The same configuration as above
 
     2. Launch the script by
      ```bash
      $ ./bin/spark-submit \
         --driver-class-path <the path to hbase>/lib/*: \
         --class examples.HBaseInput \
         <the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar \
         <host> <table> 
      ```

Example of results
==================
Assume that you have already some data in hbase as follow:

    hbase(main):028:0> scan "test"
    ROW                          COLUMN+CELL
     r1                          column=c1:a, timestamp=1420329575846, value=a1
     r1                          column=c1:b, timestamp=1420329640962, value=b1
     r2                          column=c1:a, timestamp=1420329683843, value=a2
     r3                          column=c1:,  timestamp=1420329810504, value=3

By launching `$ ./bin/spark-submit --driver-class-path <the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar <the path to hbase_input.py> localhost test c1:a`, you will get 

     {'column': 'c1:a', 'timestamp': '1420329575846', 'type': 'Put', 'value': 'a1', 'row': 'r1'}
     {'column': 'c1:a', 'timestamp': '1420329683843', 'type': 'Put', 'value': 'a2', 'row': 'r2'}
