package crystailx.scalaflagr.client

sealed trait AuthMethod

object AuthMethod {
  case class Basic(username: String, password: String) extends AuthMethod

  case class JWT(token: String) extends AuthMethod
  case object NoAuth extends AuthMethod
}
