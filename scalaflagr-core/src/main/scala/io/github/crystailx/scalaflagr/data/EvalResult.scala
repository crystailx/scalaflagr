package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Decoder

case class EvalResult(
  flagID: Option[Long] = None,
  flagKey: Option[String] = None,
  flagSnapshotID: Option[Long] = None,
  segmentID: Option[Long] = None,
  variantID: Option[Long] = None,
  variantKey: Option[String] = None,
  evalContext: Option[EvalContext] = None,
  timestamp: Option[String] = None,
  evalDebugLog: Option[EvalDebugLog] = None,
  protected val variantAttachment: Option[RawValue]
) {
  def toVariant: Option[Variant] = variantKey.map(Variant(variantID, _, variantAttachment))

  def variantAttachment[T](implicit decoder: Decoder[T]): Option[T] =
    variantAttachment.flatMap(decoder.decodeSafe(_).toOption)

}
