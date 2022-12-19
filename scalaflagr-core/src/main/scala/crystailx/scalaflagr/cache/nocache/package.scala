package crystailx.scalaflagr.cache

import crystailx.scalaflagr.data.FlagrContext

package object nocache {

  implicit val emptyCacheKey: CacheKeyCreator[String] =
    (_: FlagrContext) => ""
}
