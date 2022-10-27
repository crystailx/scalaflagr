package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.implicitConversions

trait DistributionApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig

  import config._

  private val apiBasePath: String = s"$host$basePath/flags/%d/segments/%d/distributions"

  def findDistributions(flagID: Long, segmentID: Long)(implicit
    decoder: Decoder[List[Distribution]]
  ): F[List[Distribution]] =
    get(apiBasePath format (flagID, segmentID))

  def updateDistributions(flagID: Long, segmentID: Long, body: UpdateDistributionsRequest)(implicit
    encoder: Encoder[UpdateDistributionsRequest],
    decoder: Decoder[List[Distribution]]
  ): F[List[Distribution]] =
    put(apiBasePath format (flagID, segmentID), body)
}
