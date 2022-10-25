package io.github.crystailx.scalaflagr.effect

import scala.concurrent.{ ExecutionContext, Future }

trait FunctorSyntax {

  implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
    new FutureFunctor()

}
