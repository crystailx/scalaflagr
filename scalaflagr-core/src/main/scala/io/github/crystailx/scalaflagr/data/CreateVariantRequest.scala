package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Encoder

case class CreateVariantRequest(
  key: String,
  attachment: Option[String] = None
) {
  def key(value: String): CreateVariantRequest = copy(key = value)
  def attachment(value: String): CreateVariantRequest = copy(attachment = Option(value))

  def attachment[T](value: T)(implicit encoder: Encoder[T]): CreateVariantRequest =
    copy(attachment = Option(encoder.encode(value)))
}
