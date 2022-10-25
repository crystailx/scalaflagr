package io.github.crystailx.scalaflagr.cache

import scala.util.hashing.MurmurHash3

trait SimpleCacheKeyCreator {

  implicit val simpleCacheKey: CacheKeyCreator[String] =
    (flagKey: String, entityID: String, entityType: String, entityContext: Option[String]) => {
      val bytes = entityContext.fold(Array.empty[Byte])(_.getBytes)
      val hashedContext = MurmurHash3.bytesHash(bytes)
      s"$entityType-$entityID-$flagKey-$hashedContext"
    }
}
