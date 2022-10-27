package io.github.crystailx.scalaflagr.data

case class ReorderSegmentRequest(
  segmentIDs: List[Long]
) {
  def segmentIDs(value: List[Long]): ReorderSegmentRequest = copy(segmentIDs = value)
}
