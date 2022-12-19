package crystailx.scalaflagr.cache.scaffeine

import com.github.blemale.scaffeine.{ AsyncLoadingCache, Scaffeine }
import crystailx.scalaflagr.FlagrClient
import crystailx.scalaflagr.cache.Cacher
import crystailx.scalaflagr.data.{ EvalContext, EvalResult, FlagrContext }
import crystailx.scalaflagr.effect.{ Applicative, Functor }
import crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.concurrent.{ ExecutionContext, Future }

case class ScaffeineAsyncLoadingCache[K] private (
  underlying: AsyncLoadingCache[K, EvalResult]
)(implicit ec: ExecutionContext)
    extends Cacher[K, Future] {

  override def set(key: K, evalResult: EvalResult): Future[Unit] =
    Future(underlying.put(key, Future(evalResult)))

  override def get(key: K): Future[Option[EvalResult]] =
    underlying.get(key).map(Option.apply)

  override def invalidate(key: K): Future[Unit] = Future(underlying.synchronous().invalidate(key))

  override def invalidateAll(): Future[Unit] = Future(underlying.synchronous().invalidateAll())

}

object ScaffeineAsyncLoadingCache {
  import crystailx.scalaflagr.api.evaluate

  case class ScaffeineAsyncLoadingCacheBuilder[F[_]](builder: Scaffeine[Any, Any])(implicit
    ec: ExecutionContext,
    client: FlagrClient[F],
    functor: Functor[F],
    encoder: Encoder[EvalContext],
    decoder: Decoder[EvalResult]
  ) {

    private lazy val evaluation = (context: FlagrContext) =>
      client.execute(evaluate(context.toEvalContext))

    def buildAsync(implicit handler: AsyncHandler[F]): ScaffeineAsyncLoadingCache[FlagrContext] =
      ScaffeineAsyncLoadingCache(
        builder.buildAsync[FlagrContext, EvalResult]((context: FlagrContext) =>
          handler(evaluation(context))
        )
      )

    def buildAsyncFuture(implicit
      handler: AsyncFutureHandler[F]
    ): ScaffeineAsyncLoadingCache[FlagrContext] =
      ScaffeineAsyncLoadingCache(
        builder.buildAsyncFuture[FlagrContext, EvalResult](context => handler(evaluation(context)))
      )
  }

  def builder[F[_]](builder: Scaffeine[Any, Any])(implicit
    ec: ExecutionContext,
    client: FlagrClient[F],
    functor: Functor[F],
    encoder: Encoder[EvalContext],
    decoder: Decoder[EvalResult]
  ): ScaffeineAsyncLoadingCacheBuilder[F] = ScaffeineAsyncLoadingCacheBuilder(builder)

}
