package com.crystalxyen.scalaflagr.caches

import com.desmondyeung.hashing.XxHash64
import io.circe.Json

case class SimpleCacheKey(
    flagKey: String,
    entityID: String,
    entityType: String,
    entityContext: Json
) extends CacheKey[String] {

  private def hashedContext: String =
    XxHash64.hashByteArray(entityContext.noSpaces.getBytes, 0L).toString

  override def cacheKey: String =
    s"$entityType-$entityID-$flagKey-$hashedContext"

  override def toString: String = cacheKey
}
