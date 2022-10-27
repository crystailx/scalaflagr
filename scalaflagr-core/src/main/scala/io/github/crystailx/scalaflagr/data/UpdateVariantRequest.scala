package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Encoder

case class UpdateVariantRequest(
  key: String,
  attachment: Option[String] = None
) {
  def key(value: String): UpdateVariantRequest = copy(key = value)

  def attachment(value: String): UpdateVariantRequest = copy(attachment = Option(value))

  def attachment[T](value: T)(implicit encoder: Encoder[T]): UpdateVariantRequest =
    copy(attachment = Option(encoder.encode(value)))
}
