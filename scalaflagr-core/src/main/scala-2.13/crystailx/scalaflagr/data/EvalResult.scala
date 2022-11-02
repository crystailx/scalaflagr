package crystailx.scalaflagr.data

import crystailx.scalaflagr.json.Decoder

case class EvalResult(
  flagID: Option[Long] = None,
  flagKey: Option[String] = None,
  flagSnapshotID: Option[Long] = None,
  segmentID: Option[Long] = None,
  variantID: Option[Long] = None,
  variantKey: Option[String] = None,
  evalContext: EvalContext = EvalContext(),
  timestamp: String,
  evalDebugLog: Option[EvalDebugLog] = None,
  protected val variantAttachment: Option[RawValue]
) {

  def toVariant: Option[Variant] =
    variantID.zip(variantKey).map(v => Variant(v._1, v._2, variantAttachment))

  def variantAttachment[T](implicit decoder: Decoder[T]): Option[T] =
    variantAttachment.flatMap(decoder.decodeSafe(_).toOption)

}
