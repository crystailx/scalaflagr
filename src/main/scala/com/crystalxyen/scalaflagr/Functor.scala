package com.crystalxyen.scalaflagr

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor {

  def apply[F[_]: Functor](): Functor[F] = implicitly[Functor[F]]

  implicit def FutureFunctor(
      implicit
      ec: ExecutionContext = ExecutionContext.Implicits.global
  ): Functor[Future] =
    new Functor[Future] {
      override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)

    }

  /*implicit def IdentityFunctor: Functor[Identity] = new Functor[Identity] {

    override def map[A, B](fa: Identity[A])(f: A => B): Identity[B] =
      Identity(f(fa.value))

  }*/

  implicit class RichImplicitFunctor[A, F[_]](val instance: F[A])
      extends AnyVal {

    def map[B](f: A => B)(implicit functor: Functor[F]): F[B] =
      functor.map(instance)(f)

  }

  implicit def CatsFunctor[F[_]](
      implicit catsFunctor: cats.Functor[F]
  ): Functor[F] = new Functor[F] {
    override def map[A, B](fa: F[A])(f: A => B): F[B] = catsFunctor.map(fa)(f)
  }
}
