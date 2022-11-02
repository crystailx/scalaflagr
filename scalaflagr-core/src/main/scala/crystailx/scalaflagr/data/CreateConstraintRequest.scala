package crystailx.scalaflagr.data

case class CreateConstraintRequest(
  property: String,
  operator: String,
  value: String
) {
  def property(value: String): CreateConstraintRequest = copy(property = property)
  def operator(value: String): CreateConstraintRequest = copy(operator = property)
  def value(value: String): CreateConstraintRequest = copy(value = property)
}