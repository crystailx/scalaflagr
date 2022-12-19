package crystailx.scalaflagr.effect.identity

import crystailx.scalaflagr.effect.Functor

trait FunctorSyntax {

  implicit def identityFunctor: Functor[Identity] = new IdentityFunctor()

}
