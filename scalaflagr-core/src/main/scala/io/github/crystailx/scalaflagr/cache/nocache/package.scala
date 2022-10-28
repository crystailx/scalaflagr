package io.github.crystailx.scalaflagr.cache

import io.github.crystailx.scalaflagr.data.RawValue

package object nocache {

  implicit val emptyCacheKey: CacheKeyCreator[String] =
    (_: String, _: String, _: String, _: Option[RawValue]) => ""
}
