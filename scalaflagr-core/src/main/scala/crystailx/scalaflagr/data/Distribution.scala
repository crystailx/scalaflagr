package crystailx.scalaflagr.data

case class Distribution(
  id: Option[Long] = None,
  percent: Long,
  variantKey: String,
  variantID: Long
) {
  def id(value: Long): Distribution = copy(id = Option(value))
  def percent(value: Long): Distribution = copy(percent = value)
  def variantKey(value: String): Distribution = copy(variantKey = value)
  def variantID(value: Long): Distribution = copy(variantID = value)
}
