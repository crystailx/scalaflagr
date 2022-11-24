# scalaflagr

A generic flagr evaluation client inspired by [sksamuel/elastic4s](https://github.com/sksamuel/elastic4s)

## Unsupported Features

- [ ] Authentication
- [ ] Export API

## Installation

```sbt
val scalaflagrVersion = "1.3.0"
libraryDependencies += "io.github.crystailx" %% "scalaflagr-core" % scalaflagrVersion
```

## Quick Start

### Evaluation Client

```scala
// Provide a http client and cacher

import crystailx.scalaflagr.FlagrService
import crystailx.scalaflagr.cache.simpleCacheKey
import crystailx.scalaflagr.data.EntityContext

val service = FlagrService(client, cacher)
val flagrContext = EntityContext("flag-key", entityContext)
val result = service.isEnabled(flagrContext)
```

### Management Client (for manipulating feature flags via API)

```scala
import crystailx.scalaflagr.client.sttp.SttpHttpClient

val flagrConfig = FlagrConfig("http://localhost:18000", "/api/v1")
val manager = new SttpHttpClient(flagrConfig)
manager.createTag(1L, createTagRequest value "new tag")
```

### Supported Http Clients

#### sttp v1
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-client-sttp1" % scalaflagrVersion
```

```scala
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import crystailx.scalaflagr.client.sttp.SttpHttpClient

implicit val backend = AkkaHttpBackend()
val client = FlagrClient(new SttpHttpClient(FlagrConfig()))
```

### Supported Cache

#### No Cache
```scala
import crystailx.scalaflagr.FlagrService
import crystailx.scalaflagr.cache.nocache._

val service1 = FlagrService(client) // uses NoCache by default
val service2 = FlagrService(client, NoCache())
```

#### InMemory Cache
```scala
import crystailx.scalaflagr.FlagrService
import crystailx.scalaflagr.cache.inmemory._

val service = FlagrService(client, InMemoryCache())
```

#### Redis Cache (Scredis)
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-cache-scredis" % scalaflagrVersion
```
```scala
import crystailx.scalaflagr.cache.redis._
import scredis.RedisCluster
import scala.concurrent.ExecutionContext.Implicits.global

val cluster = RedisCluster()
val service = FlagrService(client, RedisCache(cluster))
```

### Supported JSON Libraries
#### Circe
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-json-circe" % scalaflagrVersion
```
```scala
import crystailx.scalaflagr.json.circe._
```
#### Play JSON
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-json-play" % scalaflagrVersion
```
```scala
import crystailx.scalaflagr.json.play._
```
#### Jackson
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-json-jackson" % scalaflagrVersion
```
```scala
import crystailx.scalaflagr.json.jackson._
```

### Supported Effects
#### Scala Future (default)
```scala
import crystailx.scalaflagr.effect._
```
#### Twitter Future
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-effect-twitter" % scalaflagrVersion
```
```scala
import crystailx.scalaflagr.effect.twitter.syntax._
```
#### Cats
```sbt
libraryDependencies += "io.github.crystailx" %% "scalaflagr-effect-cats" % scalaflagrVersion
```
```scala
import crystailx.scalaflagr.effect.cats.syntax._
```

## Example Application
### Evaluation Service
```scala
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import crystailx.scalaflagr.auth.BasicAuthConfig
import crystailx.scalaflagr.cache.redis.{ RedisCache, _ }
import crystailx.scalaflagr.cache.simpleCacheKey
import crystailx.scalaflagr.client.sttp.SttpHttpClient
import crystailx.scalaflagr.data._
import crystailx.scalaflagr.effect._
import crystailx.scalaflagr.json.circe._
import crystailx.scalaflagr.{ FlagrClient, FlagrConfig, FlagrService }
import scredis.protocol.AuthConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object Service {

  import io.circe.generic.auto._

  case class Attachment(viewType: String)

  case class ExtraContext(age: Int)

  def main(args: Array[String]): Unit = {
    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend()
    val client = FlagrClient(
      new SttpHttpClient(
        FlagrConfig("http://localhost:18000", basicAuth = Some(BasicAuthConfig("test", "testtest")))
      )
    )
    lazy val cacher = RedisCache(
      scredis.Redis.apply("localhost", 36379, Some(AuthConfig(None, "test")))
    )
    lazy val service = new FlagrService(client, cacher)

    val evaluation = for {
      isEnabled  <- service.isEnabled(BasicContext("test-flag-key"))
      variant    <- service.getUnsafeVariant(EntityContext("test-flag-key", ExtraContext(29)))
      attachment <- Future(variant.attachment[Attachment])
    } yield (isEnabled, variant, attachment)
    val result = Await.result(evaluation, Duration.Inf)
    println(result)

    System.exit(0)
  }

}
```

### Management Client
```scala
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import crystailx.scalaflagr.api._
import crystailx.scalaflagr.api.syntax._
import crystailx.scalaflagr.auth.BasicAuthConfig
import crystailx.scalaflagr.client.sttp.SttpHttpClient
import crystailx.scalaflagr.data._
import crystailx.scalaflagr.effect._
import crystailx.scalaflagr.json.circe._
import crystailx.scalaflagr.{ FlagrClient, FlagrConfig }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object Manager {

  import io.circe.generic.auto._

  case class Attachment(viewType: String)

  case class ExtraContext(age: Int)

  def main(args: Array[String]): Unit = {
    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend()
    implicit val client: FlagrClient[Future] = FlagrClient(
      new SttpHttpClient(
        FlagrConfig("http://localhost:18000", basicAuth = Some(BasicAuthConfig("test", "testtest")))
      )
    )

    val setupFlag = for {
      flag        <- createNewFlag
      variant     <- addVariant(flag.id)
      segment     <- addSegment(flag.id)
      _           <- setDistribution(flag.id, segment.id, variant.key, variant.id)
      enabledFlag <- client.execute(enableFlag(flag.id, enable))
    } yield enabledFlag
    val flag = Await.result(setupFlag, Duration.Inf)
    println(s"flag enabled:${flag.enabled}")

    val finding = client.execute {
      findFlags(findFlagsParam key "test-flag-key")
    }
    val found = Await.result(finding, Duration.Inf)
    println(found)

    System.exit(0)
  }

  def createNewFlag(implicit client: FlagrClient[Future]): Future[Flag] =
    client.execute(
      createFlag(createFlagRequest description "test flag" key "test-flag-key")
    )

  def addVariant(flagID: Long)(implicit client: FlagrClient[Future]): Future[Variant] =
    client.execute(
      createVariant(
        flagID,
        createVariantRequest key "var1" attachment Attachment("homepage")
      )
    )

  def addSegment(flagID: Long)(implicit client: FlagrClient[Future]): Future[Segment] =
    client.execute(
      createSegment(
        flagID,
        createSegmentRequest description "all user" rolloutPercent 100
      )
    )

  def setDistribution(
    flagID: Long,
    segmentID: Long,
    variantKey: String,
    variantID: Long
  )(implicit client: FlagrClient[Future]): Future[List[Distribution]] =
    client.execute(
      updateDistributions(
        flagID,
        segmentID,
        updateDistributionsRequest distributions List(
          Distribution(None, 100, variantKey, variantID)
        )
      )
    )

}
```