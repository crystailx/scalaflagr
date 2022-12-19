package crystailx.scalaflagr

import crystailx.scalaflagr.data.FlagrContext

package object cache extends SimpleCacheKeyCreator {
  type CacheKeyCreator[K] = FlagrContext => K
}
