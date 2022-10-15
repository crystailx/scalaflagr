import Dependencies._

lazy val scala213 = "2.13.10"
lazy val scala212 = "2.12.17"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala213, scala212, scala211)
ThisBuild / organization := "com.crystailx.scalaflagr"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := scala213
ThisBuild / trackInternalDependencies := TrackLevel.TrackAlways
//ThisBuild / exportJars := true
ThisBuild / libraryDependencies ++= Dependencies.test.map(_ % Test)

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

def module(dir: String): Project = Project(dir, file(dir)) // (project in file(dir))
  .settings(
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Dependencies.log ++ Dependencies.test.map(_ % Test)
  )

lazy val moduleSettings = Seq(
  crossScalaVersions := supportedScalaVersions,
  libraryDependencies ++= Dependencies.test
    .map(_ % Test) ++ Dependencies.log //++ Dependencies.config
)

lazy val core = (project in file("scalaflagr-core"))
  .settings(moduleSettings)

lazy val sttpClient = (project in file("scalaflagr-sttp-client"))
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.sttp1)
  .dependsOn(core)

lazy val catsUtil = (project in file("scalaflagr-cats-util"))
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.cats)
  .dependsOn(core)

lazy val jsonCirce = (project in file("scalaflagr-json-circe"))
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.circe)
  .dependsOn(core)

lazy val demo = (project in file("scalaflagr-demo"))
  .settings(moduleSettings)
  .settings(
    exportToInternal := TrackLevel.NoTracking,
    libraryDependencies ++= Dependencies.sttp1Akka
  )
  .dependsOn(core, sttpClient, jsonCirce, catsUtil)

lazy val root = (project in file("."))
  .settings(
    crossScalaVersions := Nil,
    publish / skip := true,
    update / aggregate := false
  )
  .aggregate(core, sttpClient, catsUtil, jsonCirce, demo)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/crystailx/scalaflagr"),
    "scm:git:git@github.com:crystailx/scalaflagr.git"
  )
)
