package crystailx.scalaflagr.data

import java.time.Instant

case class Flag(
  id: Long,
  // unique key representation of the flag
  key: String,
  description: String,
  enabled: Boolean,
  tags: List[Tag] = Nil,
  segments: List[Segment] = Nil,
  variants: List[Variant] = Nil,
  // enabled data records will get data logging in the metrics pipeline, for example, kafka.
  dataRecordsEnabled: Boolean,
  // it will override the entityType in the evaluation logs if it's not empty
  entityType: Option[String] = None,
  // flag usage details in markdown format
  notes: Option[String] = None,
  createdBy: Option[String] = None,
  updatedBy: Option[String] = None,
  updatedAt: Instant = Instant.now()
)
