package com.crystalxyen

import io.circe._
package object scalaflagr {

  case class FlagrEndpoint(
      protocol: String,
      host: String,
      port: Int,
      prefix: Option[String]
  )

  implicit val causedByCodec: Codec[FlagrError.CausedBy] =
    derivation.deriveCodec
  implicit val failedShardCodec: Codec[FailedShard] = derivation.deriveCodec
  implicit val flagrErrorCodec: Codec[FlagrError] = derivation.deriveCodec
}
