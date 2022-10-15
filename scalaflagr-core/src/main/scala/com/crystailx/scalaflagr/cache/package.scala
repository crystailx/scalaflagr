package com.crystailx.scalaflagr


package object cache extends CacheSyntax {
  type CacheKeyCreator[K] = (String, String, String, Option[String]) => K
}
