// Your profile name of the sonatype account. The default is the same with the organization value
ThisBuild / sonatypeProfileName := "io.github.crystailx"

// To sync with Maven central, you need to supply the following information:
ThisBuild / publishMavenStyle := true

// Open-source license of your choice
ThisBuild / licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

ThisBuild / description := "A Flagr client library for Scala."

// Where is the source code hosted: GitHub or GitLab?
import xerial.sbt.Sonatype._
ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("crystailx", "scalaflagr", "changyenhtw@gmail.com"))

// or if you want to set these fields manually
ThisBuild / homepage := Some(url("https://github.com/crystailx/scalaflagr"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/crystailx/scalaflagr"),
    "scm:git:git@github.com:crystailx/scalaflagr.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "crystailx",
    name = "Chang-Yen Huang",
    email = "changyenhtw@gmail.com",
    url = url("https://github.com/crystailx")
  )
)
