package com.crystailx.scalaflagr.client

import scala.language.higherKinds

trait HttpClient[F[_]] {
  protected def send(url: String, body: String): F[String]
}
