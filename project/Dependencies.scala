import sbt._

object Dependencies {
    lazy val akkaHttp = Seq(
        "com.typesafe.akka" %% "akka-http" % "10.1.1",
        "com.typesafe.akka" %% "akka-stream" % "2.5.11"
    )
    lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
    lazy val json = Seq(
        "com.typesafe.play" %% "play-json" % "2.6.0-M1",
        "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1"
    )
    lazy val logging = Seq(
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
    )
    lazy val depPolly = "com.amazonaws" % "aws-java-sdk-polly" % "1.11.372"
}
