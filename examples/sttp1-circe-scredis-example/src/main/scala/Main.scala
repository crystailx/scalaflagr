import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import io.github.crystailx.scalaflagr.FlagrService
import io.github.crystailx.scalaflagr.cache.redis._
import io.github.crystailx.scalaflagr.cache.simpleCacheKey
import io.github.crystailx.scalaflagr.client.syntax._
import io.github.crystailx.scalaflagr.client.{
  FlagrConfig,
  SttpEvaluationClient,
  SttpManagerClient
}
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect._
import io.github.crystailx.scalaflagr.json.circe._
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
    val manager = new SttpManagerClient(FlagrConfig("http://localhost:18000"))
    val client = new SttpEvaluationClient(FlagrConfig("http://localhost:18000"))
    lazy val cacher = new RedisCache(
      scredis.Redis.apply("localhost", 36379, Some(AuthConfig(None, "test")))
    )
    lazy val service = new FlagrService(client, cacher)

    def createFlag: Future[Flag] =
      manager.createFlag(createFlagRequest description "test flag" key "test-flag")

    def addVariant(flagID: Long): Future[Variant] =
      manager.createVariant(
        flagID,
        createVariantRequest key "var1" attachment Attachment("homepage")
      )

    def addSegment(flagID: Long): Future[Segment] =
      manager.createSegment(flagID, createSegmentRequest description "all user" rolloutPercent 100)

    def setDistribution(
      flagID: Long,
      segmentID: Long,
      variantKey: String,
      variantID: Long
    ): Future[List[Distribution]] =
      manager.updateDistributions(
        flagID,
        segmentID,
        updateDistributionsRequest distributions List(
          Distribution(None, 100, variantKey, variantID)
        )
      )

    val setupFlag = for {
      flag        <- createFlag
      variant     <- addVariant(flag.id)
      segment     <- addSegment(flag.id)
      _           <- setDistribution(flag.id, segment.id, variant.key, variant.id)
      enabledFlag <- manager.enableFlag(flag.id, enable)
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
  }

}
