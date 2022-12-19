package crystailx.scalaflagr.cache

import crystailx.scalaflagr.data.{ EvalResult, FlagrContext }
import crystailx.scalaflagr.effect.identity._

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

package object scaffeine {

  type AsyncHandler[F[_]] = F[EvalResult] => EvalResult
  type AsyncFutureHandler[F[_]] = F[EvalResult] => Future[EvalResult]

  implicit val contextAsKey: CacheKeyCreator[FlagrContext] = identity[FlagrContext]

  implicit val identityEvalResultHandler: Identity[EvalResult] => EvalResult = identity

  implicit def futureEvalResultHandler(implicit
    atMost: Duration = Duration.Inf
  ): AsyncHandler[Future] = f => Await.result(f, atMost)

  implicit val future2FutureEvalResultHandler: AsyncFutureHandler[Future] =
    identity[Future[EvalResult]]
}
