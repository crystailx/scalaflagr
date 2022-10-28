package io.github.crystailx.scalaflagr.cache

import io.github.crystailx.scalaflagr.data.RawValue

import scala.util.hashing.MurmurHash3

trait SimpleCacheKeyCreator {

  implicit val simpleCacheKey: CacheKeyCreator[String] =
    (flagKey: String, entityID: String, entityType: String, entityContext: Option[RawValue]) => {
      val bytes = entityContext.getOrElse(Array.empty)
      val hashedContext = MurmurHash3.bytesHash(bytes)
      s"$entityType-$entityID-$flagKey-$hashedContext"
    }
}
