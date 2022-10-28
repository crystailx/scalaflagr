package io.github.crystailx.scalaflagr.data

import java.time.Instant

case class Flag(
  id: Option[Long] = None,
  // unique key representation of the flag
  key: Option[String] = None,
  description: String,
  enabled: Boolean,
  tags: Option[List[Tag]] = None,
  segments: Option[List[Segment]] = None,
  variants: Option[List[Variant]] = None,
  // enabled data records will get data logging in the metrics pipeline, for example, kafka.
  dataRecordsEnabled: Boolean,
  // it will override the entityType in the evaluation logs if it's not empty
  entityType: Option[String] = None,
  // flag usage details in markdown format
  notes: Option[String] = None,
  createdBy: Option[String] = None,
  updatedBy: Option[String] = None,
  updatedAt: Option[Instant] = None
)
