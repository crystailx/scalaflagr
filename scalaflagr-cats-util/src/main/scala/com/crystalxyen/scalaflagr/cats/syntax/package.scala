package com.crystalxyen.scalaflagr.cats

import cats.{
  Applicative => CatsApplicative,
  Functor => CatsFunctor,
  Monad => CatsMonad
}
import com.crystalxyen.scalaflagr.{Applicative, Functor, Monad}

import scala.language.{higherKinds, implicitConversions}
package object syntax {

  implicit def catsFunctor[F[_]](functor: CatsFunctor[F]): Functor[F] =
    new Functor[F] {
      override def map[A, B](fa: F[A])(f: A => B): F[B] = functor.map(fa)(f)
    }

  implicit def catsMonad[F[_]](monad: CatsMonad[F]): Monad[F] = new Monad[F] {
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] =
      monad.flatMap(fa)(f)
  }

  implicit def catsApplicative[F[_]](
      applicative: CatsApplicative[F]
  ): Applicative[F] =
    new Applicative[F] {
      override def pure[A](value: A): F[A] = applicative.pure(value)
    }
}
