package com.crystailx.scalaflagr.syntax

import com.crystailx.scalaflagr.effect.{ FutureMonad, Monad }

import scala.concurrent.{ ExecutionContext, Future }

trait MonadSyntax {

  implicit def futureMonad(implicit ec: ExecutionContext): Monad[Future] =
    new FutureMonad()

}
