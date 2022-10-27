package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.data.Variant.VariantAttachment
import io.github.crystailx.scalaflagr.json.Decoder

case class Variant(
  id: Option[Long] = None,
  key: String,
//  attachment: Option[Any] = None,
  private val rawValue: Option[String]
) {

  def attachment[T](implicit decoder: Decoder[VariantAttachment[T]]): Option[T] =
    rawValue.flatMap(s => decoder.decodeSafe(s).toOption.flatMap(_.variantAttachment))
}

object Variant {
  case class VariantAttachment[T](variantAttachment: Option[T])
}
