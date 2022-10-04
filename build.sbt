import Dependencies._

version := "1.0.0"
name := "scalaflagr"
organization := "com.crystalxyen.scalaflagr"
scalaVersion := "2.12.13"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.2",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.9.2"
)
libraryDependencies ++= Dependencies.test.map(_ % Test)
libraryDependencies ++= Dependencies.circe ++ Dependencies.sttp ++ Dependencies.log ++ Dependencies.config
libraryDependencies ++= Dependencies.cats ++ Dependencies.catsEffect
libraryDependencies += "com.desmondyeung.hashing" %% "scala-hashing" % "0.1.0"
resolvers ++= Seq(
  Resolver.mavenLocal
) ++ thirdPartyRepos

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

lazy val core = (project in file("scalaflagr-core"))
  .settings(
    resolvers ++= Seq(
      Resolver.mavenLocal
    ) ++ thirdPartyRepos
  )
  .settings(
    libraryDependencies ++= Dependencies.test
      .map(_ % Test) ++ Dependencies.circe ++ Dependencies.log ++ Dependencies.config
  )
  .settings(libraryDependencies += "com.desmondyeung.hashing" %% "scala-hashing" % "0.1.0")

lazy val sttpClient = (project in file("scalaflagr-sttp-client"))
  .settings(
    resolvers ++= Seq(
      Resolver.mavenLocal
    ) ++ thirdPartyRepos
  )
  .settings(libraryDependencies ++= Dependencies.sttp)
  .dependsOn(core)

lazy val catsUtil = (project in file("scalaflagr-cats-util"))
  .settings(
    resolvers ++= Seq(
      Resolver.mavenLocal
    ) ++ thirdPartyRepos
  )
  .settings(libraryDependencies ++= Dependencies.cats ++ Dependencies.catsEffect)
  .dependsOn(core)
