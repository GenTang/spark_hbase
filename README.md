spark_hbase
===========

An example in Scala of reading data saved in hbase by Spark and an example of converter for python

The example in scala transfers the data saved in hbase into RDD[String] which contains row, column:cell, timestamp, value, type. The example of converter for python transfer the data saved in hbase into string which contains the same information as the example above. We can use ast package to easily transfer this string to dictionary

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

4. Running example python script: hbase_input.py
  * if you are using `SPARK_CLASSPATH`, add `export SPARK_CLASSPATH=$SPARK_CLASSPATH":<the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar` to the `./conf/spark-env.sh`
  ```bash
  $ <the path of spark_hbase>/sbt/sbt clean assembly
  ```


Example of results
==================
