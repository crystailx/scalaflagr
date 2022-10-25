package io.github.crystailx.scalaflagr.cache

package object nocache {

  implicit val emptyCacheKey: CacheKeyCreator[String] =
    (_: String, _: String, _: String, _: Option[String]) => ""
}
