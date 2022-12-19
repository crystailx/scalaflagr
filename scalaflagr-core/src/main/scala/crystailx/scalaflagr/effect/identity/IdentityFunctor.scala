package crystailx.scalaflagr.effect.identity

import crystailx.scalaflagr.effect.Functor

class IdentityFunctor extends Functor[Identity] {
  override def map[A, B](fa: Identity[A])(f: A => B): Identity[B] = f(fa)
}
