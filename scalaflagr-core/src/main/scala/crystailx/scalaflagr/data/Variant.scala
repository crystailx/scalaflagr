package crystailx.scalaflagr.data

import crystailx.scalaflagr.json.Decoder

case class Variant(
  id: Long,
  key: String,
  protected val attachment: Option[RawValue] = None
) {

  def attachment[T](implicit decoder: Decoder[T]): Option[T] =
    attachment.flatMap(decoder.decodeSafe(_).toOption)
}
