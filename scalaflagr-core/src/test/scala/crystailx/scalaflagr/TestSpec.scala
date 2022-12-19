package crystailx.scalaflagr

import crystailx.scalaflagr.client.HttpClient
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.Future

class TestSpec extends AnyFlatSpec {
  it must "create FlagrService with default cacher and cache key creator" in {
    import crystailx.scalaflagr.cache.nocache._
    import crystailx.scalaflagr.data._
    import crystailx.scalaflagr.effect._
    import crystailx.scalaflagr.json._

    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val encoder: crystailx.scalaflagr.json.Encoder[EvalContext] =
      (body: EvalContext) => body.toString.getBytes()
    implicit val decoder: crystailx.scalaflagr.json.Decoder[EvalResult] = new Decoder[EvalResult] {
      override def decode(value: RawValue): EvalResult =
        EvalResult(timestamp = "", variantAttachment = None)

      override def decodeSafe(value: RawValue): Either[Throwable, EvalResult] =
        Left(new Exception(""))
    }
    val flagrClient: FlagrClient[Future] = FlagrClient(new HttpClient[Future] {

      override protected val config: FlagrConfig = FlagrConfig()

      override def send(request: FlagrRequest): Future[RawValue] = Future(Array.emptyByteArray)
    })

    val service = FlagrService[String, Future](flagrClient)
  }
}
