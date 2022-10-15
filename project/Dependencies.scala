import sbt._

object Dependencies {

  val thirdPartyRepos = Seq(
    "confluent-release" at "https://packages.confluent.io/maven/",
    Resolver.bintrayRepo("cakesolutions", "maven"),
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",
    "jitpack" at "https://jitpack.io"
  )

  private val log4jVersion = "2.19.0"
  private val scalaLoggingVersion = "3.9.5"
  private val circeVersion = "0.14.3"
  private val sttp1Version = "1.7.2"
  private val akkaStreamVersion = "2.6.20"
  private val catsVersion = "2.8.0"

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

  def test: Seq[ModuleID] =
    Seq(
      "org.scalatest" %% "scalatest" % "3.2.14",
      "org.scalactic" %% "scalactic" % "3.2.14",
      "org.mock-server" % "mockserver-netty" % "5.14.0",
      "org.mockito" % "mockito-core" % "4.8.0",
      "org.mockito" %% "mockito-scala" % "1.17.12"
    ) ++ cats /*.map(
      _ excludeAll (
        ExclusionRule("org.slf4j", "slf4j-log4j12"),
        ExclusionRule("org.typelevel", "cats-core_2.12"),
        ExclusionRule("com.twitter", "finagle-http_2.12"),
        ExclusionRule("io.netty", "netty-transport-native-epoll"),
        ExclusionRule("io.catbird")
      )
    ) */
}
