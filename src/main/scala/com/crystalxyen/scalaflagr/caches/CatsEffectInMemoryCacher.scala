package com.crystalxyen.scalaflagr.caches

import cats.effect.IO
import com.crystalxyen.scalaflagr.model.EvalResult

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

class CatsEffectInMemoryCacher[CK <: CacheKey[_]] extends Cacher[CK, IO] {
  private val cache: mutable.Map[CK, EvalResult] = new TrieMap[CK, EvalResult]()

  override def set(key: CK, evalResult: EvalResult): IO[Unit] =
    IO(cache.put(key, evalResult))

  override def get(key: CK): IO[Option[EvalResult]] = IO(cache.get(key))

  override def invalidate(key: CK): IO[Unit] = IO(cache.remove(key))

  override def invalidateAll(): IO[Unit] = IO(cache.clear())
}
