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
  * if you are using `SPARK_CLASSPATH`:
     1. Add `export SPARK_CLASSPATH=$SPARK_CLASSPATH":<the path to hbase>/lib/*:<the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar` to `./conf/spark-env.sh`.
  
    2. Launch the script by 
      ```bash
      $ ./bin/spark-submit <the path to hbase_input.py> <host> <table> <column>
      ```

  * you can also use `spark.executor.extraClassPath` and `--driver-class-path` (recommended)
     1. Add `spark.executor.extraClassPath <the path to hbase>/lib/*` to `spark-defaults.conf`.

     2. Launch the script by
       ```bash
        $ ./bin/spark-submit --driver-class-path <the path to spark_hbase>/target/scala-2.10/spark_hbase-assembly-1.0.jar <the path to hbase_input.py> <host> <table> <column>
       ```

Example of results
==================
