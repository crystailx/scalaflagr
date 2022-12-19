package crystailx.scalaflagr.cache.scaffeine

import com.github.blemale.scaffeine.Cache
import crystailx.scalaflagr.cache.Cacher
import crystailx.scalaflagr.data.EvalResult
import crystailx.scalaflagr.effect.Applicative

case class ScaffeineCache[K, F[_]](
  underlying: Cache[K, EvalResult]
)(implicit applicative: Applicative[F])
    extends Cacher[K, F] {

  override def set(key: K, evalResult: EvalResult): F[Unit] =
    applicative.pure(underlying.put(key, evalResult))

  override def get(key: K): F[Option[EvalResult]] =
    applicative.pure(underlying.getIfPresent(key))

  override def invalidate(key: K): F[Unit] = applicative.pure(underlying.invalidate(key))

  override def invalidateAll(): F[Unit] = applicative.pure(underlying.invalidateAll())
}
