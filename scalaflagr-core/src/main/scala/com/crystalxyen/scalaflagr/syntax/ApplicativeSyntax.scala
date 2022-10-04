package com.crystalxyen.scalaflagr.syntax

import com.crystalxyen.scalaflagr.{Applicative, FutureApplicative}

import scala.concurrent.{ExecutionContext, Future}

trait ApplicativeSyntax {

  implicit def futureApplicative(
      implicit ec: ExecutionContext
  ): Applicative[Future] =
    new FutureApplicative()

}
