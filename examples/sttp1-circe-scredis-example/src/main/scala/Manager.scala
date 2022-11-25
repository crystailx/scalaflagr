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
