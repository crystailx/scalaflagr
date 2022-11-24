package crystailx.scalaflagr.effect.twitter

import com.twitter.util.Future
import crystailx.scalaflagr.effect.Monad

class FutureMonad extends Monad[Future] {
  override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa.flatMap(f)
}
