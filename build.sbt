lazy val root = (project in file(".")).
  settings(
    name := "spark_hbase",
    version := "1.1",
    scalaVersion := "2.10.5",
    sparkVersion := "1.3.0"
  )

libraryDependencies ++= Seq(
  "org.apache.hbase" % "hbase" % "1.0.0",
  "org.apache.hbase" % "hbase-client" % "1.0.0",
  "org.apache.hbase" % "hbase-common" % "1.0.0",
  "org.apache.hbase" % "hbase-server" % "1.0.0"
)

mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "META-INF/jersey-module-version"        => MergeStrategy.first
  case _                                       => MergeStrategy.first
}
