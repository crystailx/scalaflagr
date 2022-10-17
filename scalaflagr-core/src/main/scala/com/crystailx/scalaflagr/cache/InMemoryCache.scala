package com.crystailx.scalaflagr.cache

import com.crystailx.scalaflagr.data.EvalResult
import com.crystailx.scalaflagr.effect.Applicative

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

// This is for local testing, please do not use it in production environment.
class InMemoryCache[K, F[_]](implicit
  applicative: Applicative[F]
) extends Cacher[K, F] {

  private val cache: mutable.Map[K, EvalResult] = new TrieMap[K, EvalResult]()

  override def set(key: K, evalResult: EvalResult): F[Unit] =
    cache
      .put(key, evalResult)
      .fold(throw new Exception(s"Failed to set cache key: $key"))(_ => applicative.pure(()))

  override def get(key: K): F[Option[EvalResult]] =
    applicative.pure(cache.get(key))

  override def invalidate(key: K): F[Unit] =
    applicative.pure(cache.remove(key))

  override def invalidateAll(): F[Unit] = applicative.pure(cache.clear())
}
