package com.crystalxyen.scalaflagr.syntax

import com.crystalxyen.scalaflagr.{Functor, FutureFunctor}

import scala.concurrent.{ExecutionContext, Future}

trait FunctorSyntax {

  implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
    new FutureFunctor()

}
