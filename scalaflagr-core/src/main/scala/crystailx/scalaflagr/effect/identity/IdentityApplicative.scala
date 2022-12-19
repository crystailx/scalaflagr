package crystailx.scalaflagr.effect.identity

import crystailx.scalaflagr.effect.Applicative

class IdentityApplicative extends Applicative[Identity] {
  override def pure[A](value: A): Identity[A] = value
}
