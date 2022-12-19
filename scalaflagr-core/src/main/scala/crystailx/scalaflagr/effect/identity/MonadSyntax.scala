package crystailx.scalaflagr.effect.identity

import crystailx.scalaflagr.effect.Monad

trait MonadSyntax {

  implicit def identityMonad: Monad[Identity] = new IdentityMonad()

}
