package io.github.crystailx.scalaflagr.json.circe

import io.circe.generic.semiauto.deriveCodec
import io.circe.{ Codec => CirceCodec }
import io.github.crystailx.scalaflagr.data.{ Flag, Variant }
import io.github.crystailx.scalaflagr.json.{ Attachment, CodecBase, Decoder, Encoder }

class CodecSpec extends CodecBase {
  private implicit val attachmentCodec: CirceCodec[Attachment] = deriveCodec
  override protected val flagAdaptedDecoder: Decoder[Flag] = decoder(flagDecoder)
  override protected val variantAdaptedDecoder: Encoder[Variant] = encoder(variantEncoder)
  override protected val attachmentAdaptedDecoder: Decoder[Attachment] = implicitly
  override protected val attachmentAdaptedEncoder: Encoder[Attachment] = implicitly
}
