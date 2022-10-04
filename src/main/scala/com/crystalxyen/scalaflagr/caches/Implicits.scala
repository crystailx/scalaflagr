package com.crystalxyen.scalaflagr.caches

object Implicits {
  implicit val defaultCacheKeyCreator: CacheKeyCreator[SimpleCacheKey] =
    SimpleCacheKey

}
