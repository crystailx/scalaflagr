package io.github.crystailx.scalaflagr.client


trait HttpClient[F[_]] {
  protected def send(url: String, body: String): F[String]
}
