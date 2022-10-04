package com.crystalxyen.scalaflagr.cache

import com.crystalxyen.scalaflagr.cache.CacheKey.CacheKeyCreator
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

  override def key: String =
    s"$entityType-$entityID-$flagKey-$hashedContext"

  override def toString: String = key
}

object SimpleCacheKey {

  implicit val cacheKeyCreator: CacheKeyCreator[SimpleCacheKey] =
    SimpleCacheKey(_, _, _, _)
}
