package crystailx.scalaflagr.data

case class CreateTagRequest(
  value: String
) {
  def value(value: String): CreateTagRequest = copy(value = value)
}
