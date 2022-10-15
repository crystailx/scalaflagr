import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  val thirdPartyRepos = Seq(
    "confluent-release" at "https://packages.confluent.io/maven/",
    Resolver.bintrayRepo("cakesolutions", "maven"),
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",
    "jitpack" at "https://jitpack.io"
  )

  private val log4jVersion = "2.17.1"

  def log = Seq(
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    // log errors to Sentry
    "io.sentry" % "sentry-log4j2" % "5.6.1"
  )

  def akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.6.17",
    "com.typesafe.akka" %% "akka-protobuf" % "2.6.17",
    "com.typesafe.akka" %% "akka-stream" % "2.6.17"
  )

  def akkaExclusion = Seq(
    ExclusionRule("com.typesafe.akka", "akka-actor_2.12"),
    ExclusionRule("com.typesafe.akka", "akka-stream_2.12"),
    ExclusionRule("com.typesafe.akka", "akka-protobuf_.12")
  )

  private val circeDerivationVersion = "0.13.0-M5"

  def circe = Seq(
    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-generic-extras" % "0.13.1-M4",
    "io.circe" %% "circe-derivation" % circeDerivationVersion,
    "io.circe" %% "circe-derivation-annotations" % circeDerivationVersion
  )

  def config =
    Seq(
      "com.typesafe" % "config" % "1.4.1",
      "io.circe" %% "circe-config" % "0.8.0"
    ) ++ circe

  private val sttpVersion = "1.7.2"

  def sttp =
    (Seq(
      "com.softwaremill.sttp" %% "core" % sttpVersion,
      "com.softwaremill.sttp" %% "akka-http-backend" % sttpVersion,
      "com.softwaremill.sttp" %% "circe" % sttpVersion,
      "io.zipkin.brave" % "brave" % "5.13.3",
      "com.softwaremill.sttp" %% "brave-backend" % "1.7.2",
      "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % "2.16.3",
      "io.zipkin.reporter2" % "zipkin-reporter-brave" % "2.16.3"
    ) ++ akka).map(
      _ excludeAll (
        akkaExclusion: _*
      )
    )

  def cats = Seq(
    "org.typelevel" %% "cats-core" % "2.6.1"
  )

  def catsEffect = Seq(
    "org.typelevel" %% "cats-effect" % "3.2.9"
  )

  def test =
    Seq(
      "org.scalatest" %% "scalatest" % "3.2.9",
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.mock-server" % "mockserver-netty" % "5.11.2",
      "org.mockito" % "mockito-core" % "3.11.2",
      "org.mockito" %% "mockito-scala" % "1.16.37",
      "com.softwaremill.diffx" %% "diffx-scalatest" % "0.4.1",
      "org.rauschig" % "jarchivelib" % "1.0.0"
    ).map(
      _ excludeAll (
        ExclusionRule("org.slf4j", "slf4j-log4j12"),
        ExclusionRule("org.typelevel", "cats-core_2.12"),
        ExclusionRule("com.twitter", "finagle-http_2.12"),
        ExclusionRule("io.netty", "netty-transport-native-epoll"),
        ExclusionRule("io.catbird")
      )
    ) ++ cats
}
