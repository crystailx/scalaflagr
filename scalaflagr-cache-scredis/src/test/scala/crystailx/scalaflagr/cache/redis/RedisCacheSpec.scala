package crystailx.scalaflagr.cache.redis

import crystailx.scalaflagr.data.{ EvalResult, RawValue }
import crystailx.scalaflagr.json.{ Decoder, Encoder }
import org.scalatest.flatspec.AnyFlatSpec
import scredis.RedisCluster

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class RedisCacheSpec extends AnyFlatSpec {
  it must "create cluster cache" in {
    val cluster = RedisCluster()
    implicit val encoder: Encoder[EvalResult] = (body: EvalResult) => body.toString.getBytes()
    implicit val decoder: Decoder[EvalResult] = new Decoder[EvalResult] {
      override def decode(value: RawValue): EvalResult =
        EvalResult(timestamp = "", variantAttachment = None)

      override def decodeSafe(value: RawValue): Either[Throwable, EvalResult] =
        Try(EvalResult(timestamp = "", variantAttachment = None)).toEither
    }
    RedisCache(cluster)
  }
}
