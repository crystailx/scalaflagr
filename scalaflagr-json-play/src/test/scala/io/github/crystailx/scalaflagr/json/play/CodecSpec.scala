package io.github.crystailx.scalaflagr.json.play

import io.github.crystailx.scalaflagr.data.{ Flag, Variant }
import io.github.crystailx.scalaflagr.json.{ Attachment, CodecBase, Decoder, Encoder }
import play.api.libs.json.{ Format => PlayCodec, Json }

class CodecSpec extends CodecBase {
  private implicit val attachmentCodec: PlayCodec[Attachment] = Json.format
  override protected val flagAdaptedDecoder: Decoder[Flag] = decoder(flagDecoder)
  override protected val variantAdaptedDecoder: Encoder[Variant] = encoder(variantEncoder)
  override protected val attachmentAdaptedDecoder: Decoder[Attachment] = implicitly
  override protected val attachmentAdaptedEncoder: Encoder[Attachment] = implicitly
}
