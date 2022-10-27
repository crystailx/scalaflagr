package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.Decoder

import scala.language.implicitConversions

trait HealthApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig
  import config._

  private val apiBasePath: String = s"$host$basePath/health"

  def healthStatus(implicit decoder: Decoder[Health]): F[Health] =
    get(apiBasePath)
}
