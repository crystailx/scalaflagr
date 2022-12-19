package crystailx.scalaflagr.cache.scaffeine

import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }
import crystailx.scalaflagr.FlagrClient
import crystailx.scalaflagr.cache.Cacher
import crystailx.scalaflagr.data.{ EvalContext, EvalResult, FlagrContext }
import crystailx.scalaflagr.effect.{ Applicative, Functor }
import crystailx.scalaflagr.json.{ Decoder, Encoder }

case class ScaffeineLoadingCache[K, F[_]] private (
  underlying: LoadingCache[K, EvalResult]
)(implicit applicative: Applicative[F])
    extends Cacher[K, F] {

  override def set(key: K, evalResult: EvalResult): F[Unit] =
    applicative.pure(underlying.put(key, evalResult))

  override def get(key: K): F[Option[EvalResult]] =
    applicative.pure(Option(underlying.get(key)))

  override def invalidate(key: K): F[Unit] = applicative.pure(underlying.invalidate(key))

  override def invalidateAll(): F[Unit] = applicative.pure(underlying.invalidateAll())

}

object ScaffeineLoadingCache {
  import crystailx.scalaflagr.api.evaluate

  def apply[F[_]](
    builder: Scaffeine[Any, Any],
    handler: F[EvalResult] => EvalResult
  )(implicit
    applicative: Applicative[F],
    client: FlagrClient[F],
    functor: Functor[F],
    encoder: Encoder[EvalContext],
    decoder: Decoder[EvalResult]
  ): ScaffeineLoadingCache[FlagrContext, F] = {
    val loadingCache = builder.build[FlagrContext, EvalResult]((context: FlagrContext) =>
      handler(client.execute(evaluate(context.toEvalContext)))
    )
    ScaffeineLoadingCache(loadingCache)
  }
}
