
import AssemblyKeys._

lazy val root = (project in file(".")).
  settings(
    name := "pythonconverters",
    version := "1.0",
    scalaVersion := "2.10.4"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.2.0" % "provided",
  "org.apache.hbase" % "hbase" % "0.98.8-hadoop2",
  "org.apache.hbase" % "hbase-client" % "0.98.8-hadoop2",
  "org.apache.hbase" % "hbase-common" % "0.98.8-hadoop2",
  "org.apache.hbase" % "hbase-server" % "0.98.8-hadoop2"
)

assemblySettings

mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "META-INF/jersey-module-version"        => MergeStrategy.first
  case _                                       => MergeStrategy.first
}
