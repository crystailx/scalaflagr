package crystailx.scalaflagr.effect.twitter

import com.twitter.util.Future
import crystailx.scalaflagr.effect.Applicative

class FutureApplicative extends Applicative[Future] {
  override def pure[A](value: A): Future[A] = Future.value(value)
}
