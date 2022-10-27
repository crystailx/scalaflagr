package io.github.crystailx.scalaflagr.data

case class CreateSegmentRequest(
  description: String,
  rolloutPercent: Long
) {
  def description(value: String): CreateSegmentRequest = copy(description = value)
  def rolloutPercent(value: Long): CreateSegmentRequest = copy(rolloutPercent = value)
}
