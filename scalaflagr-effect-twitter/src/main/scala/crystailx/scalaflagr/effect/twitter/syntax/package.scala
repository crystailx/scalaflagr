package crystailx.scalaflagr.effect.twitter

import com.twitter.util.Future
import crystailx.scalaflagr.effect.{ Applicative, Functor, Monad }

package object syntax {

  implicit def futureFunctor: Functor[Future] = new FutureFunctor

  implicit def futureMonad: Monad[Future] = new FutureMonad

  implicit def futureApplicative: Applicative[Future] = new FutureApplicative
}
