package com.crystalxyen.scalaflagr

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait Monad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object Monad {

  def apply[F[_]: Monad](): Monad[F] = implicitly[Monad[F]]

  implicit def FutureMonad(
      implicit
      ec: ExecutionContext = ExecutionContext.Implicits.global
  ): Monad[Future] =
    new Monad[Future] {

      override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] =
        fa.flatMap(f)
    }

  implicit class RichImplicitMonad[A, F[_]](val instance: F[A]) extends AnyVal {

    def flatMap[B](f: A => F[B])(implicit monad: Monad[F]): F[B] =
      monad.flatMap(instance)(f)
  }

  implicit def CatsMonad[F[_]](
      implicit
      catsMonad: cats.Monad[F]
  ): Monad[F] = new Monad[F] {
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] =
      catsMonad.flatMap(fa)(f)
  }
}
