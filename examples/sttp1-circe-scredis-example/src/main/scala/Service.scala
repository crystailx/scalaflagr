import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import crystailx.scalaflagr.auth.BasicAuthConfig
import crystailx.scalaflagr.cache.simpleCacheKey
import crystailx.scalaflagr.client.sttp.SttpHttpClient
import crystailx.scalaflagr.data._
import crystailx.scalaflagr.json.circe._
import crystailx.scalaflagr.cache.scredis._
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
