package io.github.crystailx.scalaflagr.data

case class UpdateSegmentRequest(
  description: String,
  rolloutPercent: Long
) {
  def description(value: String): UpdateSegmentRequest = copy(description = value)
  def rolloutPercent(value: Long): UpdateSegmentRequest = copy(rolloutPercent = value)
}