import Dependencies._
import com.jsuereth.sbtpgp.PgpKeys.publishSigned

lazy val scala213 = "2.13.10"
lazy val scala212 = "2.12.17"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = Seq(scala213, scala212 /*, scala211*/ )
ThisBuild / organization := "io.github.crystailx"
ThisBuild / version := "1.2.2"
ThisBuild / scalaVersion := scala213
ThisBuild / crossScalaVersions := supportedScalaVersions
ThisBuild / trackInternalDependencies := TrackLevel.TrackAlways
//ThisBuild / exportJars := true
ThisBuild / libraryDependencies ++= Dependencies.test.map(_ % Test)

// sonatype publish
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / publishTo := sonatypePublishToBundle.value

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

lazy val moduleSettings = Seq(
  crossScalaVersions := supportedScalaVersions,
  libraryDependencies ++= Dependencies.test
    .map(_ % Test) ++ Dependencies.log
)

lazy val noPublishSettings = Seq(
  publish / skip := true,
  publish := {},
  publishLocal := {},
  publishSigned := {}
)

lazy val core = (project in file("scalaflagr-core"))
  .settings(name := "scalaflagr-core")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor == 12 =>
      Seq("com.chuusai" %% "shapeless" % "2.3.10")
    case _ => Seq.empty
  }))

lazy val coreWithTest = core % "compile->compile;test->test"

lazy val sttp1Client = (project in file("scalaflagr-client-sttp1"))
  .settings(name := "scalaflagr-client-sttp1")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.sttp1)
  .dependsOn(core)

lazy val catsEffect = (project in file("scalaflagr-effect-cats"))
  .settings(name := "scalaflagr-effect-cats")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.cats)
  .dependsOn(core)

lazy val jsonCirce = (project in file("scalaflagr-json-circe"))
  .settings(name := "scalaflagr-json-circe")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.circe)
  .dependsOn(coreWithTest)

lazy val jsonPlay = (project in file("scalaflagr-json-play"))
  .settings(name := "scalaflagr-json-play")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.playJson)
  .dependsOn(coreWithTest)

lazy val jsonJackson = (project in file("scalaflagr-json-jackson"))
  .settings(name := "scalaflagr-json-jackson")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.jackson)
  .dependsOn(coreWithTest)

lazy val redisCache = (project in file("scalaflagr-cache-scredis"))
  .settings(name := "scalaflagr-cache-scredis")
  .settings(moduleSettings)
  .settings(libraryDependencies ++= Dependencies.redis)
  .dependsOn(core)

lazy val root = (project in file("."))
  .settings(noPublishSettings)
  .settings(
    crossScalaVersions := Nil,
    update / aggregate := false
  )
  .aggregate(core, sttp1Client, catsEffect, jsonCirce, jsonPlay, jsonJackson, redisCache)
