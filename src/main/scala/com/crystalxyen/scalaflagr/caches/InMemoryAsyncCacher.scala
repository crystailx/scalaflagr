package com.crystalxyen.scalaflagr.caches

import com.crystalxyen.scalaflagr.model.EvalResult

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InMemoryAsyncCacher[CK <: CacheKey[_]] extends Cacher[CK, Future] {
  /*.onComplete {
    case Success(_) => logger.debug(s"set cache: $key")
    case Failure(exception) =>
      logger.error(s"set cache failed: $key", exception)
  }*/

  private val cache: mutable.Map[CK, EvalResult] = new TrieMap[CK, EvalResult]()

  override def set(key: CK, evalResult: EvalResult): Future[Unit] = Future {
    cache
      .put(key, evalResult)
      .fold(throw new Exception(s"Failed to set cache key: $key"))(_ => ())
  }

  override def get(key: CK): Future[Option[EvalResult]] = Future(cache.get(key))

  override def invalidate(key: CK): Future[Unit] = Future {
    cache.remove(key)
  }

  override def invalidateAll(): Future[Unit] = Future(cache.clear())
}
