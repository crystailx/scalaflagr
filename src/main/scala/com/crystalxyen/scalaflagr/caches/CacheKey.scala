package com.crystalxyen.scalaflagr.caches

trait CacheKey[K] {
  def cacheKey: K
}
