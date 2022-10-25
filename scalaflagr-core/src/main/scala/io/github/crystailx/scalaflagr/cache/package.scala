package io.github.crystailx.scalaflagr

package object cache extends SimpleCacheKeyCreator {
  type CacheKeyCreator[K] = (String, String, String, Option[String]) => K
}
