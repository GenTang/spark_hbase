import sys
import json

from pyspark import SparkContext

"""
Create test data in HBase first:
hbase(main):016:0> create 'test', 'c1'
hbase(main):017:0> put 'test', 'r1', 'c1:a', 'a1'
hbase(main):018:0> put 'test', 'r1', 'c1:b', 'b1'
hbase(main):019:0> put 'test', 'r2', 'c1:a', 'a2'
hbase(main):020:0> put 'test', 'r3', 'c1', '3'
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
      Usage: hbase_inputformat <host> <table> <column>
      Run with example jar:
      ./bin/spark-submit --driver-class-path <the path to spark_hbase.jar> \
      /path/to/examples/hbase_inputformat.py <host> <table> <column>
      Assumes you have some data in HBase already, running on <host>, in <table> at <column>
      More information is available at https://github.com/GenTang/spark_hbase
        """
    exit(-1)
  host = sys.argv[1]
  table = sys.argv[2]
  column = sys.argv[3]
  sc = SparkContext(appName="HBaseInputFormat")

  # Other options for configuring scan behavior are available. More information available at
  # https://github.com/apache/hbase/blob/master/hbase-server/src/main/java/org/apache/hadoop/hbase/mapreduce/TableInputFormat.java 
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

  
  hbase_rdd = hbase_rdd.flatMapValues(lambda v: v.split("\n")).mapValues(json.loads)
  # hbase_rdd is a RDD[dict]

  output = hbase_rdd.collect()
  for (k, v) in output:
    print (k, v)

  sc.stop()
