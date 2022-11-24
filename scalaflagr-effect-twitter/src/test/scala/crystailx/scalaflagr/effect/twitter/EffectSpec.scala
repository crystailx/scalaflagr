package crystailx.scalaflagr.effect.twitter

import com.twitter.util.{ Await, Future }
import crystailx.scalaflagr.effect.{ Applicative, Functor, Monad }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class EffectSpec extends AnyFlatSpec with Matchers {
  it must "have functor for twitter future" in {
    import syntax.futureFunctor
    noException must be thrownBy Await.result(implicitly[Functor[Future]].map(Future(1))(_ + 1))
  }
  it must "have monad for twitter future" in {
    import syntax.futureMonad
    noException must be thrownBy Await.result(
      implicitly[Monad[Future]].flatMap(Future(1))(v => Future(v + 1))
    )
  }
  it must "have applicative for twitter future" in {
    import syntax.futureApplicative
    noException must be thrownBy Await.result(implicitly[Applicative[Future]].pure(1))
  }
}
