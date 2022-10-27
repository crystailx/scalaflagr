package io.github.crystailx.scalaflagr.data

case class EvalDebugLog(
  segmentDebugLogs: Option[List[SegmentDebugLog]] = None,
  msg: Option[String] = None
)
