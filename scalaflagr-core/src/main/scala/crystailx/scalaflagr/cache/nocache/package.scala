package crystailx.scalaflagr.cache

import crystailx.scalaflagr.data.RawValue

package object nocache {

  implicit val emptyCacheKey: CacheKeyCreator[String] =
    (_: String, _: String, _: String, _: Option[RawValue]) => ""
}
