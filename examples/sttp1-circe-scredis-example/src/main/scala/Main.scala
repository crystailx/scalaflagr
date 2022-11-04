import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.{ SttpBackend, UriInterpolator }
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import crystailx.scalaflagr.api._
import crystailx.scalaflagr.api.syntax._
import crystailx.scalaflagr.auth.BasicAuthConfig
import crystailx.scalaflagr.cache.redis.{ RedisCache, _ }
import crystailx.scalaflagr.cache.simpleCacheKey
import crystailx.scalaflagr.client.SttpHttpClient
import crystailx.scalaflagr.data._
import crystailx.scalaflagr.effect._
import crystailx.scalaflagr.json.circe._
import crystailx.scalaflagr.{ FlagrClient, FlagrConfig, FlagrService }
import scredis.protocol.AuthConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object Main {

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
    lazy val cacher = new RedisCache(
      scredis.Redis.apply("localhost", 36379, Some(AuthConfig(None, "test")))
    )
    lazy val service = new FlagrService(client, cacher)

    def createNewFlag: Future[Flag] =
      client.execute(
        createFlag(createFlagRequest description "test flag" key "test-flag")
      )

    def addVariant(flagID: Long): Future[Variant] =
      client.execute(
        createVariant(
          flagID,
          createVariantRequest key "var1" attachment Attachment("homepage")
        )
      )

    def addSegment(flagID: Long): Future[Segment] =
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
    ): Future[List[Distribution]] =
      client.execute(
        updateDistributions(
          flagID,
          segmentID,
          updateDistributionsRequest distributions List(
            Distribution(None, 100, variantKey, variantID)
          )
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

    val evaluation = for {
      isEnabled  <- service.isEnabled(BasicContext(flag.key))
      variant    <- service.getUnsafeVariant(EntityContext(flag.key, ExtraContext(29)))
      attachment <- Future(variant.attachment[Attachment])
    } yield (isEnabled, variant, attachment)
    val result = Await.result(evaluation, Duration.Inf)
    println(result)
    val finding = client.execute {
      findFlags(findFlagsParam key "test-flag")
    }
    val found = Await.result(finding, Duration.Inf)
    println(found)
    System.exit(0)
  }

}
