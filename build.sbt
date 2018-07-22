import Dependencies._
//import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.MappingsHelper._

name := "workoutgen"
organization in ThisBuild := "com.marcinkossakowski"
scalaVersion in ThisBuild := "2.12.6"

lazy val root = project
    .in(file("."))
    .settings(
            name := "root",
        )
    .aggregate(
        api,
        core
    )

lazy val api = (project in file("api"))
    .enablePlugins(JavaAppPackaging)
    .settings(
        mappings in Universal ++= directory((resourceDirectory in (core, Compile)).value).map(t => (t._1, t._2.replace("resources", "core/src/main/resources"))),
        name := "api",
        libraryDependencies += scalaTest % Test,
        libraryDependencies ++= json,
        libraryDependencies ++= akkaHttp,
        libraryDependencies ++= logging,
        libraryDependencies += depPolly
    ).dependsOn(core)

lazy val core = (project in file("core"))
    .enablePlugins(JavaAppPackaging)
//    .enablePlugins(UniversalPlugin)
    .settings(
        mappings in Universal ++= directory((resourceDirectory in Compile).value),
        name := "core",
        libraryDependencies += scalaTest % Test,
        libraryDependencies ++= logging
  )

