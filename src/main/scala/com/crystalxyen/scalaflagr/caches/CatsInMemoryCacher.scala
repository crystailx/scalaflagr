package com.crystalxyen.scalaflagr.caches

import cats.Id
import com.crystalxyen.scalaflagr.model.EvalResult

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

class CatsInMemoryCacher[CK <: CacheKey[_]] extends Cacher[CK, Id] {
  private val cache: mutable.Map[CK, EvalResult] = new TrieMap[CK, EvalResult]()

  override def set(key: CK, evalResult: EvalResult): Id[Unit] =
    cache.put(key, evalResult)

  override def get(key: CK): Id[Option[EvalResult]] = cache.get(key)

  override def invalidate(key: CK): Id[Unit] = cache.remove(key)

  override def invalidateAll(): Id[Unit] = cache.clear()
}
