package examples.pythonconverters

import scala.collection.JavaConversions._

import org.apache.spark.api.python.Converter
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.KeyValue.Type


class HBaseResultToBufferConverter extends Converter[Any, Buffer[String]]{
  override def convert(obj: Any): Buffer[String] = {
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
    output.mkString(" ")
  }
}


