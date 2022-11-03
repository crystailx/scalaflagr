package crystailx.scalaflagr.client

sealed trait HttpMethod

object HttpMethod {
  case object Get extends HttpMethod
  case object Delete extends HttpMethod
  case object Post extends HttpMethod
  case object Put extends HttpMethod
  case object Patch extends HttpMethod
}
