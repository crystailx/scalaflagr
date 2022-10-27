package io.github.crystailx.scalaflagr.data

case class CreateFlagRequest(
  description: String,
  // unique key representation of the flag
  key: Option[String] = None,
  // template for flag creation
  template: Option[String] = None
) {
  def description(value: String): CreateFlagRequest = copy(description = value)
  def key(value: String): CreateFlagRequest = copy(key = Option(value))
  def template(value: String): CreateFlagRequest = copy(template = Option(value))
}
