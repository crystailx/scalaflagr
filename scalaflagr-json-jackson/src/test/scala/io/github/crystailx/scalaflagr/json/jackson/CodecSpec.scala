package io.github.crystailx.scalaflagr.json.jackson

import io.github.crystailx.scalaflagr.data.{ Flag, Variant }
import io.github.crystailx.scalaflagr.json.{ Attachment, CodecBase, Decoder, Encoder }

class CodecSpec extends CodecBase {
  override protected val flagAdaptedDecoder: Decoder[Flag] = implicitly
  override protected val variantAdaptedDecoder: Encoder[Variant] = implicitly
  override protected val attachmentAdaptedDecoder: Decoder[Attachment] = implicitly
  override protected val attachmentAdaptedEncoder: Encoder[Attachment] = implicitly
}
