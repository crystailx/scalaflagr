package com.crystalxyen.scalaflagr.cache

import io.circe.Json

import scala.language.higherKinds

trait CacheKey[K] {

  def key: K
}

object CacheKey {

  trait CacheKeyCreator[CK <: CacheKey[_]] {
    def create(
        flagKey: String,
        entityID: String,
        entityType: String,
        entityContext: Json
    ): CK
  }

}
