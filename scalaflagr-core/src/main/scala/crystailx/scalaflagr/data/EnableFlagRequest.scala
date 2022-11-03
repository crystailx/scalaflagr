package crystailx.scalaflagr.data

case class EnableFlagRequest(
  enabled: Boolean
) {
  def enabled(value: Boolean): EnableFlagRequest = copy(enabled = value)
}
