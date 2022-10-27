package io.github.crystailx.scalaflagr.json.circe

import io.circe.generic.semiauto._
import io.circe.{ parser, Decoder => CirceDecoder }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.json.Decoder

import java.text.DateFormat
import java.util.Date

trait DecoderAdapter {

  implicit def decoder[T: CirceDecoder]: Decoder[T] =
    new Decoder[T] {
      override def decode(body: String): T = parser.decode[T](body).fold(throw _, identity)

      override def decodeSafe(body: String): Either[Exception, T] = parser.decode[T](body)
    }

  // some default decoders for evaluation to avoid boilerplate
  implicit val evalContextDecoder: CirceDecoder[EvalContext] = deriveDecoder
  implicit val segmentDebugLogDecoder: CirceDecoder[SegmentDebugLog] = deriveDecoder
  implicit val evalDebugLogDecoder: CirceDecoder[EvalDebugLog] = deriveDecoder
  implicit val evalResultDecoder: CirceDecoder[EvalResult] = deriveDecoder

  // some default decoders for management to avoid boilerplate
  implicit val constraintDecoder: CirceDecoder[Constraint] = deriveDecoder
  implicit val distributionDecoder: CirceDecoder[Distribution] = deriveDecoder
  implicit val tagDecoder: CirceDecoder[Tag] = deriveDecoder
  implicit val segmentDecoder: CirceDecoder[Segment] = deriveDecoder
  implicit val variantDecoder: CirceDecoder[Variant] = deriveDecoder
  implicit val flagDecoder: CirceDecoder[Flag] = deriveDecoder

}
