// Use sbt-spark-package plugin to assembly package
// You may use this file to add plugin dependencies for sbt.
resolvers += "Spark Packages repo" at "https://dl.bintray.com/spark-packages/maven/"

addSbtPlugin("org.spark-packages" %% "sbt-spark-package" % "0.2.1")
