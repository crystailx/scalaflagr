package com.crystailx.scalaflagr

import scala.language.higherKinds

package object cache extends CacheSyntax {
  type CacheKeyCreator[K] = (String, String, String, Option[String]) => K
}
