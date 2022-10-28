package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Encoder

case class CreateVariantRequest(
  key: String,
  protected val attachment: Option[RawValue] = None
) {
  def key(value: String): CreateVariantRequest = copy(key = value)
  def attachment(value: RawValue): CreateVariantRequest = copy(attachment = Option(value))

  def attachment(value: String): CreateVariantRequest = copy(attachment = Option(value.getBytes))

  def attachment[T](value: T)(implicit encoder: Encoder[T]): CreateVariantRequest =
    copy(attachment = Option(encoder.encode(value)))
}
