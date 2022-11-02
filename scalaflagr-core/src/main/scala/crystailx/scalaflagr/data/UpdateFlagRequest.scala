package crystailx.scalaflagr.data

case class UpdateFlagRequest(
  description: Option[String] = None,
  // enabled data records will get data logging in the metrics pipeline, for example, kafka.
  dataRecordsEnabled: Option[Boolean] = None,
  // it will overwrite entityType into evaluation logs if it's not empty
  entityType: Option[String] = None,
  enabled: Option[Boolean] = None,
  key: Option[String] = None,
  notes: Option[String] = None
) {
  def description(value: String): UpdateFlagRequest = copy(description = Option(value))

  def dataRecordsEnabled(value: Boolean): UpdateFlagRequest =
    copy(dataRecordsEnabled = Option(value))
  def entityType(value: String): UpdateFlagRequest = copy(entityType = Option(value))
  def enabled(value: Boolean): UpdateFlagRequest = copy(enabled = Option(value))
  def key(value: String): UpdateFlagRequest = copy(key = Option(value))
  def notes(value: String): UpdateFlagRequest = copy(notes = Option(value))
}
