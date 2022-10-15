package com.crystailx.scalaflagr.syntax

import com.crystailx.scalaflagr.effect.{ Functor, FutureFunctor }

import scala.concurrent.{ ExecutionContext, Future }

trait FunctorSyntax {

  implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
    new FutureFunctor()

}
