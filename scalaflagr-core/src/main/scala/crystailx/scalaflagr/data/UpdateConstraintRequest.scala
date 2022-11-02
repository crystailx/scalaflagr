package crystailx.scalaflagr.data

case class UpdateConstraintRequest(
  property: String,
  operator: String,
  value: String
) {
  def property(value: String): UpdateConstraintRequest = copy(property = property)
  def operator(value: String): UpdateConstraintRequest = copy(operator = property)
  def value(value: String): UpdateConstraintRequest = copy(value = property)
}