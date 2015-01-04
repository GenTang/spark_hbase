

import sys
import ast

from pyspark import SparkContext

"""
Create test data in HBase first:
hbase(main):016:0> create 'test', 'c1'
0 row(s) in 1.0430 seconds
hbase(main):017:0> put 'test', 'r1', 'c1:a', 'a1'
0 row(s) in 0.0130 seconds
hbase(main):018:0> put 'test', 'r1', 'c1:b', 'b1'
0 row(s) in 0.0030 seconds
hbase(main):019:0> put 'test', 'r2', 'c1:a', 'a2'
0 row(s) in 0.0050 seconds
hbase(main):020:0> put 'test', 'r3', 'c1', '3'
0 row(s) in 0.0110 seconds
hbase(main):028:0> scan "test"
ROW                          COLUMN+CELL
 r1                          column=c1:a, timestamp=1420329575846, value=a1
 r1                          column=c1:b, timestamp=1420329640962, value=b1
 r2                          column=c1:a, timestamp=1420329683843, value=a2
 r3                          column=c1:,  timestamp=1420329810504, value=3
"""
if __name__ == "__main__":
  if len(sys.argv) != 4:
    print >> sys.stderr, """
      Usage: hbase_inputformat <host> <table>
      Run with example jar:
      ./bin/spark-submit --driver-class-path /path/to/example.jar \
      /path/to/examples/hbase_inputformat.py <host> <table> <column>
      Assumes you have some data in HBase already, running on <host>, in <table>
        """
    exit(-1)
  host = sys.argv[1]
  table = sys.argv[2]
  column = sys.argv[3]
  sc = SparkContext(appName="HBaseInputFormat")

  # Other options for configuring scan behavior are available. More information available at
  # http://github.com/apache/hbase/blob/master/hbase-server/src/main/java/org/apache/hadoop/hbase/mapreduce/TableInputFormat.java 
  conf = {
      "hbase.zookeeper.quorum": host, 
      "hbase.mapreduce.inputtable": table,
      "hbase.mapreduce.scan.columns": column}
  keyConv = "examples.pythonconverters.ImmutableBytesWritableToStringConverter"
  valueConv = "examples.pythonconverters.HBaseResultToStringConverter"

  hbase_rdd = sc.newAPIHadoopRDD(
      "org.apache.hadoop.hbase.mapreduce.TableInputFormat",
      "org.apache.hadoop.hbase.io.ImmutableBytesWritable",
      "org.apache.hadoop.hbase.client.Result",
      keyConverter=keyConv,
      valueConverter=valueConv,
      conf=conf)

  hbase_rdd = hbase_rdd.flatMap(lambda line: line[1].split(" ")).map(ast.literal_eval)

  output = hbase_rdd.collect()
  for record in output:
    print record

  sc.stop()
