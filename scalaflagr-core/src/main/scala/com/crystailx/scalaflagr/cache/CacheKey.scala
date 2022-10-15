package com.crystailx.scalaflagr.cache

import scala.language.higherKinds

trait CacheKey[K] {

  def key: K
}
