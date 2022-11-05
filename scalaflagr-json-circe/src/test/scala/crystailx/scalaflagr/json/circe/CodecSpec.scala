package crystailx.scalaflagr.json.circe

import crystailx.scalaflagr.data.{ Flag, Variant }
import crystailx.scalaflagr.json.{ Attachment, CodecBase, Decoder, Encoder }
import io.circe.generic.semiauto.deriveCodec
import io.circe.{ Codec => CirceCodec }

class CodecSpec extends CodecBase {
  private implicit val attachmentCodec: CirceCodec[Attachment] = deriveCodec
  override protected val flagAdaptedDecoder: Decoder[Flag] = decoderAdapter(flagDecoder)
  override protected val variantAdaptedDecoder: Encoder[Variant] = encoderAdapter(variantEncoder)
  override protected val attachmentAdaptedDecoder: Decoder[Attachment] = implicitly
  override protected val attachmentAdaptedEncoder: Encoder[Attachment] = implicitly
}
