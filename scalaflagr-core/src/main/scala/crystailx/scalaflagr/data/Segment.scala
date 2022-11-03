package crystailx.scalaflagr.data

case class Segment(
  id: Long,
  description: String,
  constraints: List[Constraint] = Nil,
  distributions: List[Distribution] = Nil,
  rank: Long,
  rolloutPercent: Long
)
