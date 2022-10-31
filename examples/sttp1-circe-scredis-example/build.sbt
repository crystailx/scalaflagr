organization := "io.github.crystailx"
version := "1.0.0"
scalaVersion := "2.13.10"
name := "sttp-circe-scredis-example"

val scalaflagrVersion = "1.1.0"
val sttp1Version = "1.7.2"
val akkaStreamVersion = "2.7.0"

libraryDependencies ++= Seq(
  "io.github.crystailx" %% "scalaflagr-core" % scalaflagrVersion,
  "io.github.crystailx" %% "scalaflagr-json-circe" % scalaflagrVersion,
  "io.github.crystailx" %% "scalaflagr-cache-scredis" % scalaflagrVersion,
  "io.github.crystailx" %% "scalaflagr-client-sttp1" % scalaflagrVersion,
  "com.softwaremill.sttp" %% "akka-http-backend" % sttp1Version,
  "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion
)