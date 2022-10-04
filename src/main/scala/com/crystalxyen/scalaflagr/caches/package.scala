package com.crystalxyen.scalaflagr

import io.circe.Json

package object caches {
  type CacheKeyCreator[K] = (String, String, String, Json) => K
  def inMemoryCacherInstance: InMemoryCacher[SimpleCacheKey] =
    new InMemoryCacher
  def inMemoryAsyncCacherInstance: InMemoryAsyncCacher[SimpleCacheKey] =
    new InMemoryAsyncCacher
}
