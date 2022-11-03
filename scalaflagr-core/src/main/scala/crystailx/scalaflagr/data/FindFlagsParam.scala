package crystailx.scalaflagr.data

case class FindFlagsParam(
  limit: Option[Long] = None,
  enabled: Option[Boolean] = None,
  description: Option[String] = None,
  tags: Option[String] = None,
  descriptionLike: Option[String] = None,
  key: Option[String] = None,
  offset: Option[Long] = None,
  preload: Option[Boolean] = None,
  deleted: Option[Boolean] = None
) {
  def limit(value: Long): FindFlagsParam = copy(limit = Option(value))
  def enabled(value: Boolean): FindFlagsParam = copy(enabled = Option(value))
  def description(value: String): FindFlagsParam = copy(description = Option(value))
  def tags(value: String): FindFlagsParam = copy(tags = Option(value))
  def descriptionLike(value: String): FindFlagsParam = copy(descriptionLike = Option(value))
  def key(value: String): FindFlagsParam = copy(key = Option(value))
  def offset(value: Long): FindFlagsParam = copy(offset = Option(value))
  def preload(value: Boolean): FindFlagsParam = copy(preload = Option(value))
  def deleted(value: Boolean): FindFlagsParam = copy(deleted = Option(value))
}
