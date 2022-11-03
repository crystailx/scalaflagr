package crystailx.scalaflagr

import crystailx.scalaflagr.auth.{ BasicAuthConfig, HeaderIdentifierConfig }

case class FlagrConfig(
  host: String = "http://localhost:18000",
  basePath: String = "/api/v1",
  headerIdentifier: Option[HeaderIdentifierConfig] = None,
  basicAuth: Option[BasicAuthConfig] = None
)
