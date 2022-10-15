package com.crystailx.scalaflagr.json.circe

import com.crystailx.scalaflagr.data.{ EvalContext, EvalDebugLog, EvalResult, SegmentDebugLog }
import com.crystailx.scalaflagr.json.Decoder
import io.circe.derivation.deriveDecoder

trait CirceDecoder {

  implicit def decoder[T: io.circe.Decoder]: Decoder[T] =
    new Decoder[T] {
      override def decode(body: String): T = io.circe.parser.decode[T](body).fold(throw _, identity)

      override def decodeSafe(body: String): Either[Exception, T] = io.circe.parser.decode[T](body)
    }

  implicit val evalContextDecoder: io.circe.Decoder[EvalContext] = deriveDecoder
  implicit val segmentDebugLogDecoder: io.circe.Decoder[SegmentDebugLog] = deriveDecoder
  implicit val evalDebugLogDecoder: io.circe.Decoder[EvalDebugLog] = deriveDecoder
  implicit val evalResultDecoder: io.circe.Decoder[EvalResult] = deriveDecoder

}
