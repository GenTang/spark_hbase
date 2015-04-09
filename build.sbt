lazy val root = (project in file(".")).
  settings(
    name := "spark_hbase",
    version := "1.0",
    scalaVersion := "2.10.5",
    sparkVersion := "1.3.0"
  )

libraryDependencies ++= Seq(
  "org.apache.hbase" % "hbase" % "0.98.8-hadoop2",
  "org.apache.hbase" % "hbase-client" % "0.98.8-hadoop2",
  "org.apache.hbase" % "hbase-common" % "0.98.8-hadoop2",
  "org.apache.hbase" % "hbase-server" % "0.98.8-hadoop2"
)

mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "META-INF/jersey-module-version"        => MergeStrategy.first
  case _                                       => MergeStrategy.first
}
