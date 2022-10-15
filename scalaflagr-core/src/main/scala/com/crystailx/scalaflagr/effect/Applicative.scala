package com.crystailx.scalaflagr.effect

import scala.language.higherKinds

trait Applicative[F[_]] {
  def pure[A](value: A): F[A]
}

object Applicative {

  def apply[F[_]: Applicative](): Applicative[F] = implicitly[Applicative[F]]

}
