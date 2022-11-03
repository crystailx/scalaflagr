package crystailx.scalaflagr.cache

trait CacheKey[K] {
  def key: K
}
