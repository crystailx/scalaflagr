package com.crystailx.scalaflagr.effect

import scala.concurrent.{ ExecutionContext, Future }

class FutureApplicative(implicit ec: ExecutionContext) extends Applicative[Future] {
  override def pure[A](value: A): Future[A] = Future(value)
}
