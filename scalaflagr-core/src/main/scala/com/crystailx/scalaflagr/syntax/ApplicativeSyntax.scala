package com.crystailx.scalaflagr.syntax

import com.crystailx.scalaflagr.effect.{ Applicative, FutureApplicative }

import scala.concurrent.{ ExecutionContext, Future }

trait ApplicativeSyntax {

  implicit def futureApplicative(
    implicit
    ec: ExecutionContext
  ): Applicative[Future] =
    new FutureApplicative()

}
