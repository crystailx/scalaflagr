import sbt._

object Dependencies {

  private val log4jVersion = "2.19.0"
  private val scalaLoggingVersion = "3.9.5"
  private val circeVersion = "0.14.3"
  private val sttp1Version = "1.7.2"
  private val akkaStreamVersion = "2.6.20"
  private val catsVersion = "2.8.0"
  private val redisVersion = "2.4.3"
  private val playJsonVersion = "2.9.3"
  private val jacksonVersion = "2.13.4"
  private val scalatestVersion = "3.2.14"
  private val scalacticVersion = scalatestVersion
  private val scalamockVersion = "5.2.0"

  def log: Seq[ModuleID] = Seq(
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  )

  def circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )

  def playJson: Seq[ModuleID] = Seq("com.typesafe.play" %% "play-json" % playJsonVersion)

  def jackson: Seq[ModuleID] = Seq(
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion
  )

  def sttp1: Seq[ModuleID] =
    Seq(
      "com.softwaremill.sttp" %% "core" % sttp1Version
    )

  def sttp1Akka: Seq[ModuleID] = Seq(
    "com.softwaremill.sttp" %% "akka-http-backend" % sttp1Version,
    "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion
  )

  def cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % catsVersion
  )

  def redis: Seq[ModuleID] = Seq(
    "com.github.scredis" %% "scredis" % redisVersion
  )

  def test: Seq[ModuleID] =
    Seq(
      "org.scalatest" %% "scalatest" % scalatestVersion,
      "org.scalactic" %% "scalactic" % scalacticVersion,
      "org.scalamock" %% "scalamock" % scalamockVersion,
      "org.mock-server" % "mockserver-netty" % "5.14.0"
    )
}
