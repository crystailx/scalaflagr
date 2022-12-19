package crystailx.scalaflagr.effect.identity

import crystailx.scalaflagr.effect.Applicative

trait ApplicativeSyntax {

  implicit def identityApplicative: Applicative[Identity] = new IdentityApplicative()

}
