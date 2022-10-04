package com.crystalxyen.scalaflagr

import io.circe.Decoder.Result
import io.circe._
package object model {

  implicit val evalContextCodec: Codec[EvalContext] = derivation.deriveCodec

  implicit val evalResultCodec: Codec[EvalResult] = derivation.deriveCodec

  implicit val jsonDecoder: Decoder[Json] = (c: HCursor) => {
    lazy val emptyJson: Result[Json] = Right(Json.Null)
    c.keys
      .filter(_.nonEmpty)
      .fold(emptyJson)(_ => c.focus.fold(emptyJson)(Right.apply))
  }

  implicit val evalDebugLogCodec: Codec[EvalDebugLog] = derivation.deriveCodec

  implicit val segmentDebugLogCodec: Codec[SegmentDebugLog] =
    derivation.deriveCodec
}
