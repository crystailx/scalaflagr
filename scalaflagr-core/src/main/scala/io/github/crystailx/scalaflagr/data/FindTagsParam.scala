package io.github.crystailx.scalaflagr.data

case class FindTagsParam(
  limit: Option[Long] = None,
  offset: Option[Long] = None,
  valueLike: Option[String] = None
) {
  def limit(value: Long): FindTagsParam = copy(limit = Option(value))
  def offset(value: Long): FindTagsParam = copy(offset = Option(value))
  def valueLike(value: String): FindTagsParam = copy(valueLike = Option(value))
}
