package crystailx.scalaflagr.effect.twitter

import com.twitter.util.Future
import crystailx.scalaflagr.effect.Functor

class FutureFunctor extends Functor[Future]{
  override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)
}
