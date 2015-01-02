/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples.pythonconverters

import scala.collection.JavaConversions._

import org.apache.spark.api.python.Converter
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.KeyValue.Type


/**
 * Implementation of [[org.apache.spark.api.python.Converter]] that converts all 
 * the records in an HBase Result to a String. In the String, it contains row, column,
 * timesstamp, type and value
 */


class HBaseResultToBufferConverter extends Converter[Any, String]{
  override def convert(obj: Any): String = {
    import collection.JavaConverters._

    val result = obj.asInstanceOf[Result]
    val output = result.list.asScala.map(record =>
        "row=%s,column=%s,timestamp=%s,type=%s,value=%s".format(
          Bytes.toStringBinary(record.getRow),
          Bytes.toStringBinary(record.getFamily) + ":" + Bytes.toStringBinary(record.getQualifier),
          record.getTimestamp.toString,
          Bytes.toStringBinary(record.getValue),
          Type.codeToType(record.getType)
        ))
    // output is an instance of [[Buffer[String]]]
    output.mkString(" ")
  }
}


