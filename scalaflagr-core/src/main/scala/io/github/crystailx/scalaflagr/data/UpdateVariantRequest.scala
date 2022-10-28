package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Encoder

case class UpdateVariantRequest(
  key: String,
  protected val attachment: Option[RawValue] = None
) {
  def key(value: String): UpdateVariantRequest = copy(key = value)

  def attachment(value: RawValue): UpdateVariantRequest = copy(attachment = Option(value))
  def attachment(value: String): UpdateVariantRequest = copy(attachment = Option(value.getBytes))

  def attachment[T](value: T)(implicit encoder: Encoder[T]): UpdateVariantRequest =
    copy(attachment = Option(encoder.encode(value)))
}
