package com.crystalxyen.scalaflagr.syntax

import com.crystalxyen.scalaflagr.{FutureMonad, Monad}

import scala.concurrent.{ExecutionContext, Future}

trait MonadSyntax {

  implicit def futureMonad(implicit ec: ExecutionContext): Monad[Future] =
    new FutureMonad()

}
