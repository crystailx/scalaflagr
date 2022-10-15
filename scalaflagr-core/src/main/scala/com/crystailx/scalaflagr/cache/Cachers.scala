package com.crystailx.scalaflagr.cache

import com.crystailx.scalaflagr.cache.Cachers._
import com.crystailx.scalaflagr.effect.Applicative
trait Cachers extends InMemoryCacher with NoCacheCacher {}

object Cachers {

  trait InMemoryCacher {

    implicit def inMemoryCacher[K, F[_]: Applicative]: Cacher[K, F] = new InMemoryCache()
  }

  trait NoCacheCacher extends CacheKeyCreators.EmptyCacheKeyCreator {

    implicit def noCacheCacher[K, F[_]: Applicative]: NoCache[K, F] = new NoCache()
  }
}
