package crystailx.scalaflagr.json.play

import crystailx.scalaflagr.data.{ Flag, Variant }
import crystailx.scalaflagr.json.{ Attachment, CodecBase, Decoder, Encoder }
import play.api.libs.json.{ Json, Format => PlayCodec }

class CodecSpec extends CodecBase {
  private implicit val attachmentCodec: PlayCodec[Attachment] = Json.format
  override protected val flagAdaptedDecoder: Decoder[Flag] = decoderAdapter(flagDecoder)
  override protected val variantAdaptedDecoder: Encoder[Variant] = encoderAdapter(variantEncoder)
  override protected val attachmentAdaptedDecoder: Decoder[Attachment] = implicitly
  override protected val attachmentAdaptedEncoder: Encoder[Attachment] = implicitly
}
