package crystailx.scalaflagr.effect.identity

import crystailx.scalaflagr.effect.Monad

class IdentityMonad extends Monad[Identity] {
  override def flatMap[A, B](fa: Identity[A])(f: A => Identity[B]): Identity[B] = f(fa)
}
