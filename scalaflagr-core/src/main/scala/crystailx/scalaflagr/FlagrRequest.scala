package crystailx.scalaflagr

import crystailx.scalaflagr.client.{ AuthMethod, HttpMethod, Identifier }
import crystailx.scalaflagr.data.RawValue

case class FlagrRequest(
  method: HttpMethod,
  path: String,
  body: Option[RawValue] = None,
  params: Map[String, String] = Map.empty,
  headers: Map[String, String] = Map.empty,
  identifier: Option[Identifier] = None,
  authMethod: Option[AuthMethod] = None
)
