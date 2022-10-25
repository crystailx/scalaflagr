package io.github.crystailx.scalaflagr.effect

import scala.concurrent.{ ExecutionContext, Future }

class FutureFunctor(implicit ec: ExecutionContext) extends Functor[Future] {
  override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)
}
