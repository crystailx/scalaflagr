package io.github.crystailx.scalaflagr.cache

trait CacheKey[K] {
  def key: K
}
