package io.github.crystailx.scalaflagr.json.circe

import io.github.crystailx.scalaflagr.data.{
  EvalContext,
  EvalDebugLog,
  EvalResult,
  SegmentDebugLog
}
import io.github.crystailx.scalaflagr.json.Decoder
import io.circe.generic.semiauto._
import io.circe.{ parser, Decoder => CirceDecoder }

trait DecoderAdapter {

  implicit def decoder[T: CirceDecoder]: Decoder[T] =
    new Decoder[T] {
      override def decode(body: String): T = parser.decode[T](body).fold(throw _, identity)

      override def decodeSafe(body: String): Either[Exception, T] = parser.decode[T](body)
    }

  // some default decoders to avoid boilerplate
  implicit val evalContextDecoder: CirceDecoder[EvalContext] = deriveDecoder
  implicit val segmentDebugLogDecoder: CirceDecoder[SegmentDebugLog] = deriveDecoder
  implicit val evalDebugLogDecoder: CirceDecoder[EvalDebugLog] = deriveDecoder
  implicit val evalResultDecoder: CirceDecoder[EvalResult] = deriveDecoder

}
