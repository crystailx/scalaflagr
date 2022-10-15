import Dependencies._

//version := "1.0.0"
//name := "scalaflagr"
lazy val scala213 = "2.13.10"
lazy val scala212 = "2.12.17"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala213, scala212, scala211)
ThisBuild / organization := "com.crystailx.scalaflagr"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := scala213

ThisBuild / libraryDependencies ++= Dependencies.test.map(_ % Test)

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

def module(dir: String): Project = Project(dir, file(dir)) // (project in file(dir))
  /*.settings(
    resolvers ++= Seq(
      Resolver.mavenLocal
    ) ++ thirdPartyRepos
  )*/
  .settings(
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Dependencies.test
      .map(_ % Test) ++ Dependencies.log ++ Dependencies.config
  )

def mod(proj: Project): Project =
  proj.settings(
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++=
      Dependencies.test.map(_ % Test) ++ Dependencies.log ++ Dependencies.config
  )

lazy val core = mod(project in file("scalaflagr-core"))
  .settings(libraryDependencies += "com.desmondyeung.hashing" %% "scala-hashing" % "0.1.0")

lazy val sttpClient = module("scalaflagr-sttp-client")
  .settings(libraryDependencies ++= Dependencies.sttp)
  .dependsOn(core)

lazy val catsUtil = module("scalaflagr-cats-util")
  .settings(libraryDependencies ++= Dependencies.cats ++ Dependencies.catsEffect)
  .dependsOn(core)

lazy val jsonCirce = module("scalaflagr-json-circe")
  .settings(libraryDependencies ++= Dependencies.circe)
  .dependsOn(core)

lazy val demo = module("scalaflagr-demo")
  .dependsOn(core, sttpClient, jsonCirce, catsUtil)

lazy val root = (project in file("."))
  .settings(
    Seq(
      crossScalaVersions := Nil,
      publish / skip := true
    )
  )
  .aggregate(core, sttpClient, catsUtil, jsonCirce, demo)
