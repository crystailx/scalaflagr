package io.github.crystailx.scalaflagr.json.circe

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ Encoder => CirceEncoder }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.json.Encoder

trait EncoderAdapter {

  implicit def encoder[T](implicit encoder: CirceEncoder[T]): Encoder[T] =
    (body: T) => encoder.apply(body).noSpaces

  // some default encoders for evaluation to avoid boilerplate
  implicit val evalContextCirceEncoder: CirceEncoder[EvalContext] = deriveEncoder
  implicit val segmentDebugLogEncoder: CirceEncoder[SegmentDebugLog] = deriveEncoder
  implicit val evalDebugLogEncoder: CirceEncoder[EvalDebugLog] = deriveEncoder
  implicit val evalResultEncoder: CirceEncoder[EvalResult] = deriveEncoder

  // some default encoders for management to avoid boilerplate
  implicit val constraintEncoder: CirceEncoder[Constraint] = deriveEncoder
  implicit val distributionEncoder: CirceEncoder[Distribution] = deriveEncoder
  implicit val tagEncoder: CirceEncoder[Tag] = deriveEncoder
  implicit val segmentEncoder: CirceEncoder[Segment] = deriveEncoder
  implicit val variantEncoder: CirceEncoder[Variant] = deriveEncoder
  implicit val flagEncoder: CirceEncoder[Flag] = deriveEncoder
}
