package crystailx.scalaflagr.client

sealed trait AuthMethod

object AuthMethod {
  case class Header(userId: String) extends AuthMethod
  case object Basic extends AuthMethod
}
