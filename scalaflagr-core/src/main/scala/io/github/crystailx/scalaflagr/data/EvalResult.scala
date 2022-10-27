package io.github.crystailx.scalaflagr.data

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
  private val rawValue: Option[String] = None
) {
  def toVariant: Option[Variant] = variantKey.map(Variant(variantID, _, rawValue))

}
