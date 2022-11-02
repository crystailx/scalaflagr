package crystailx.scalaflagr.data

case class Constraint(
  id: Option[Long] = None,
  property: String,
  operator: String,
  value: String
)
