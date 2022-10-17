package com.crystailx.scalaflagr.cache

import com.crystailx.scalaflagr.cache.CacheKeyCreators.{
  EmptyCacheKeyCreator,
  SimpleCacheKeyCreator
}

import scala.util.hashing.MurmurHash3

trait CacheKeyCreators extends EmptyCacheKeyCreator with SimpleCacheKeyCreator {}

object CacheKeyCreators {

  trait EmptyCacheKeyCreator {

    implicit val emptyCacheKey: CacheKeyCreator[String] =
      (_: String, _: String, _: String, _: Option[String]) => ""
  }

  trait SimpleCacheKeyCreator {

    implicit val simpleCacheKey: CacheKeyCreator[String] =
      (flagKey: String, entityID: String, entityType: String, entityContext: Option[String]) => {
        val bytes = entityContext.fold(Array.empty[Byte])(_.getBytes)
        val hashedContext = MurmurHash3.bytesHash(bytes)
        s"$entityType-$entityID-$flagKey-$hashedContext"
      }
  }
}
