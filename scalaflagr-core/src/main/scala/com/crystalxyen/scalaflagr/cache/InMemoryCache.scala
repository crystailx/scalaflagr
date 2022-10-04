package com.crystalxyen.scalaflagr.cache

import com.crystalxyen.scalaflagr.Applicative
import com.crystalxyen.scalaflagr.data.EvalResult

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.language.higherKinds

class InMemoryCache[K, F[_]](
    implicit
    applicative: Applicative[F]
) extends Cacher[K, F] {

  private val cache: mutable.Map[K, EvalResult] = new TrieMap[K, EvalResult]()

  override def set(key: CacheKey[K], evalResult: EvalResult): F[Unit] =
    cache
      .put(key.key, evalResult)
      .fold(throw new Exception(s"Failed to set cache key: $key"))(
        _ => applicative.pure(())
      )

  override def get(key: CacheKey[K]): F[Option[EvalResult]] =
    applicative.pure(cache.get(key.key))

  override def invalidate(key: CacheKey[K]): F[Unit] =
    applicative.pure(cache.remove(key.key))

  override def invalidateAll(): F[Unit] = applicative.pure(cache.clear())
}
