package io.github.crystailx.scalaflagr.effect


trait Monad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object Monad {

  def apply[F[_]: Monad](): Monad[F] = implicitly[Monad[F]]

}
