package com.crystalxyen.scalaflagr

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.higherKinds

trait Applicative[F[_]] {
  def pure[A](value: A): F[A]
}

object Applicative {

  implicit def futureApplicative: Applicative[Future] =
    new Applicative[Future] {
      override def pure[A](value: A): Future[A] = Future(value)
    }

  implicit def CatsApplicative[F[_]](
      implicit
      catsApplicative: cats.Applicative[F]
  ): Applicative[F] = new Applicative[F] {
    override def pure[A](value: A): F[A] = catsApplicative.pure(value)
  }
}
