package io.github.crystailx.scalaflagr.json.circe

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ Encoder => CirceEncoder }
import io.github.crystailx.scalaflagr.data.{
  EvalContext,
  EvalDebugLog,
  EvalResult,
  SegmentDebugLog
}
import io.github.crystailx.scalaflagr.json.Encoder

trait EncoderAdapter {

  implicit def encoder[T](implicit encoder: CirceEncoder[T]): Encoder[T] =
    (body: T) => encoder.apply(body).noSpaces

  // some default encoders to avoid boilerplate
  implicit val evalContextCirceEncoder: CirceEncoder[EvalContext] = deriveEncoder
  implicit val segmentDebugLogEncoder: CirceEncoder[SegmentDebugLog] = deriveEncoder
  implicit val evalDebugLogEncoder: CirceEncoder[EvalDebugLog] = deriveEncoder
  implicit val evalResultEncoder: CirceEncoder[EvalResult] = deriveEncoder
}
