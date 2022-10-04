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

  val jacksonVersion = "2.11.1"

  def log = Seq(
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    // log errors to Sentry
    "io.sentry" % "sentry-log4j2" % "5.6.1"
  )

  val finagleVersion = "21.6.0"

  def netty = Seq(
    "io.netty" % "netty-tcnative" % "2.0.40.Final",
    "io.netty" % "netty-tcnative-boringssl-static" % "2.0.40.Final",
    "io.netty" % "netty-transport-native-epoll" % "4.1.59.Final" classifier "linux-aarch_64",
    "io.netty" % "netty-all" % "4.1.59.Final"
  )

  def catbird = Seq(
    // compatibility library to Cats
    "io.catbird" %% "catbird-effect" % "21.5.0"
  )

  def finagle =
    netty ++ Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
      "com.twitter" %% "finagle-stats" % finagleVersion,
      "com.twitter" %% "twitter-server" % finagleVersion,
      "com.twitter" %% "finagle-http" % finagleVersion,
      "io.zipkin.finagle2" %% "zipkin-finagle-http" % "2.2.1",
      "com.github.idiotech" % "finagle-prometheus" % "05292e7c92"
    ) ++ catbird

  def thrift =
    finagle ++ (
      Seq(
        "com.twitter" %% "finagle-thriftmux" % finagleVersion,
        "com.twitter" %% "scrooge-core" % finagleVersion,
        // utilities to (de)serialize Thrift classes to JSON
        "com.gu" %% "fezziwig" % "1.4",
        "com.stripe" %% "scrooge-shapes" % "0.1.0",
        "com.chuusai" %% "shapeless" % "2.3.7"
      ) ++ catbird
    ).map(
      _ excludeAll (
        ExclusionRule(
          "io.netty",
          "netty-transport-native-epoll"
        )
      )
    )

  def enum = Seq(
    "com.beachape" %% "enumeratum" % "1.7.0",
    "com.beachape" %% "enumeratum-circe" % "1.7.0"
  )

  def retry = Seq(
    "com.softwaremill.retry" %% "retry" % "0.3.3"
  )

  def goggles = Seq(
    "com.github.kenbot" %% "goggles-dsl" % "1.0",
    "com.github.kenbot" %% "goggles-macros" % "1.0"
  )

  private val elasticVersion = "7.10.9"

  def elastic = Seq(
    "org.elasticsearch.client" % "elasticsearch-rest-client" % "7.10.2",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.1",
    "com.sksamuel.elastic4s" %% "elastic4s-core" % elasticVersion,
    "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elasticVersion,
    "com.sksamuel.elastic4s" %% "elastic4s-client-sttp" % elasticVersion,
    "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % elasticVersion
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

  private val confluentAvroVersion = "6.2.0"
  private val kafkaVersion = "2.8.0"
  private val avro4sVersion = "4.0.10"

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

  val finchVersion = "0.31.0"

  def finch =
    finagle ++
      Seq(
        "com.github.finagle" %% "finch-core" % finchVersion,
        "com.github.finagle" %% "finch-circe" % finchVersion
      ).map(
        _ excludeAll (
          ExclusionRule("org.typelevel", "cats-effect_2.12"),
          ExclusionRule("org.typelevel", "cats-core_2.12"),
          ExclusionRule("com.twitter", "finagle-http_2.12"),
          ExclusionRule("io.netty", "netty-transport-native-epoll"),
          ExclusionRule("io.catbird"),
        )
      ) ++ cats ++ catbird

  def redis =
    (Seq(
      "com.linecorp.scratchcard" %% "scredis" % "1.0.2"
    ) ++ akka).map(
      _ excludeAll (
        akkaExclusion: _*
      )
    )

  def conversion = Seq(
    "org.julienrf" %% "enum" % "3.1",
    "io.scalaland" %% "chimney" % "0.6.1"
  )

  def i18n = Seq(
    "com.osinka.i18n" %% "scala-i18n" % "1.0.3"
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
      // TODO replace with docker image
      "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.11",
      "com.dimafeng" %% "testcontainers-scala-elasticsearch" % "0.39.11",
      "org.rauschig" % "jarchivelib" % "1.0.0",
      ("com.github.finagle" %% "finch-test" % finchVersion) exclude ("com.twitter", "finagle-http_2.12") exclude ("org.typelevel", "cats-laws_2.12"),
      "com.github.sebruck" %% "scalatest-embedded-redis" % "0.4.0",
      "org.apache.kafka" % "kafka-streams-test-utils" % kafkaVersion
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
