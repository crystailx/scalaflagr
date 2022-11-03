package crystailx.scalaflagr.effect

import scala.concurrent.{ ExecutionContext, Future }

class FutureMonad(implicit ec: ExecutionContext) extends Monad[Future] {

  override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] =
    fa.flatMap(f)
}
