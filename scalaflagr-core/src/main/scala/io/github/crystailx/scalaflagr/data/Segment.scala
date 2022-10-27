package io.github.crystailx.scalaflagr.data

case class Segment(
  id: Option[Long] = None,
  description: String,
  constraints: Option[List[Constraint]] = None,
  distributions: Option[List[Distribution]] = None,
  rank: Long,
  rolloutPercent: Long
)
