package crystailx.scalaflagr.cache.scaffeine

import crystailx.scalaflagr.client.HttpClient
import crystailx.scalaflagr.data._
import crystailx.scalaflagr.effect.identity.Identity
import crystailx.scalaflagr.json.{ Decoder, Encoder }
import crystailx.scalaflagr.{ FlagrConfig, FlagrRequest }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TestData {
  val key1: FlagrContext = BasicContext("flag1")

  val value1: EvalResult =
    EvalResult(flagKey = Some("flag1"), timestamp = "", variantAttachment = None)
  val key2: FlagrContext = BasicContext("flag2")

  val value2: EvalResult =
    EvalResult(flagKey = Some("flag2"), timestamp = "", variantAttachment = None)
  val key3: FlagrContext = BasicContext("flag3")

  val value3: EvalResult =
    EvalResult(flagKey = Some("flag3"), timestamp = "", variantAttachment = None)

  val identityHttpClient: HttpClient[Identity] = new HttpClient[Identity] {
    override protected val config: FlagrConfig = FlagrConfig()

    override def send(request: FlagrRequest): Identity[RawValue] =
      request.body.map(new String(_)) match {
        case Some(flagKey) => flagKey.getBytes
        case _             => throw new Exception("Failed to find flag")
      }
  }

  val futureHttpClient: HttpClient[Future] = new HttpClient[Future] {
    override protected val config: FlagrConfig = FlagrConfig()

    override def send(request: FlagrRequest): Future[RawValue] =
      request.body.map(new String(_)) match {
        case Some(flagKey) => Future(flagKey.getBytes)
        case _             => Future.failed(new Exception("Failed to find flag"))
      }
  }

  implicit val encoder: Encoder[EvalContext] = {
    case EvalContext(_, _, _, _, _, Some(flagKey), _, _) => flagKey.getBytes
    case _                                               => Array.emptyByteArray
  }

  implicit val decoder: Decoder[EvalResult] = new Decoder[EvalResult] {

    override def decode(value: RawValue): EvalResult =
      EvalResult(
        flagKey = Option(value).filter(_.nonEmpty).map(new String(_)),
        timestamp = "",
        variantAttachment = None
      )

    override def decodeSafe(value: RawValue): Either[Throwable, EvalResult] =
      Right(
        EvalResult(
          flagKey = Option(value).filter(_.nonEmpty).map(new String(_)),
          timestamp = "",
          variantAttachment = None
        )
      )
  }
}
