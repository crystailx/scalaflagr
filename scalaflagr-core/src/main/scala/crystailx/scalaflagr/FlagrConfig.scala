package crystailx.scalaflagr

import crystailx.scalaflagr.auth.{ BasicAuthConfig, HeaderAuthConfig }

case class FlagrConfig(
  host: String = "http://localhost:18000",
  basePath: String = "/api/v1",
  headerAuth: Option[HeaderAuthConfig] = None,
  basicAuth: Option[BasicAuthConfig] = None
)
