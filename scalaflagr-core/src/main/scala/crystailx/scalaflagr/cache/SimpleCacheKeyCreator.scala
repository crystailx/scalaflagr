package crystailx.scalaflagr.cache

import crystailx.scalaflagr.data.FlagrContext

import scala.util.hashing.MurmurHash3

trait SimpleCacheKeyCreator {

  implicit val simpleCacheKey: CacheKeyCreator[String] =
    (context: FlagrContext) => {
      import context._
      val bytes = entityContext.getOrElse(Array.empty)
      val hashedContext = MurmurHash3.bytesHash(bytes)
      s"$entityType-$entityID-$flagKey-$hashedContext"
    }
}
