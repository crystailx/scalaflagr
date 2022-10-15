package com.crystailx.scalaflagr.data

import com.crystailx.scalaflagr.data.Variant.VariantAttachment
import com.crystailx.scalaflagr.json.Decoder

case class Variant(variantKey: String, private val rawValue: Option[String]) {

  def attachments[T](implicit decoder: Decoder[VariantAttachment[T]]): Option[T] =
    rawValue.flatMap(s => decoder.decodeSafe(s).toOption.flatMap(_.variantAttachment))
}

object Variant {
  case class VariantAttachment[T](variantAttachment: Option[T])
}
