package com.crystalxyen.scalaflagr

import scala.language.higherKinds

package object cache {

  def newInMemoryCacher[K, F[_]: Applicative]: Cacher[K, F] =
    new InMemoryCache[K, F]()
}
