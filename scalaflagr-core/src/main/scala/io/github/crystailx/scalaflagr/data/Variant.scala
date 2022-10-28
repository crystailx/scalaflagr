package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Decoder

case class Variant(
  id: Option[Long] = None,
  key: String,
  protected val attachment: Option[RawValue] = None
) {

  def attachment[T](implicit decoder: Decoder[T]): Option[T] =
    attachment.flatMap(decoder.decodeSafe(_).toOption)
}
