package io.github.crystailx.scalaflagr

import io.github.crystailx.scalaflagr.data.RawValue

package object cache extends SimpleCacheKeyCreator {
  type CacheKeyCreator[K] = (String, String, String, Option[RawValue]) => K
}
