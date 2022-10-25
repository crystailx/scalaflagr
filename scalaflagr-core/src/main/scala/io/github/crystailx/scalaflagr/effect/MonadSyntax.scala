package io.github.crystailx.scalaflagr.effect

import scala.concurrent.{ ExecutionContext, Future }

trait MonadSyntax {

  implicit def futureMonad(implicit ec: ExecutionContext): Monad[Future] =
    new FutureMonad()

}
