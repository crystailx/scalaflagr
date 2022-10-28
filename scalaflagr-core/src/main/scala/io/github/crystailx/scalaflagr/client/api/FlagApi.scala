package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.implicitConversions

trait FlagApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig
  import config._

  private val apiBasePath: String = s"$host$basePath/flags"

  def createFlag(flagRequest: CreateFlagRequest)(implicit
    encoder: Encoder[CreateFlagRequest],
    decoder: Decoder[Flag]
  ): F[Flag] =
    post(apiBasePath, flagRequest)

  def deleteFlag(flagID: Long): F[Unit] =
    delete(s"$apiBasePath/$flagID")

  def findFlags(param: FindFlagsParam)(implicit decoder: Decoder[List[Flag]]): F[List[Flag]] =
    get(apiBasePath, buildParamsMap(param))

  def flag(flagID: Long)(implicit decoder: Decoder[Flag]): F[Flag] =
    get(s"$apiBasePath/$flagID")

  def flagEntityTypes(implicit decoder: Decoder[List[String]]): F[List[String]] =
    get(s"$apiBasePath/entity_types")

  def flagSnapshots(flagID: Long)(implicit
    decoder: Decoder[List[FlagSnapshot]]
  ): F[List[FlagSnapshot]] =
    get(s"$apiBasePath/$flagID/snapshots")

  def updateFlag(flagID: Long, flagRequest: UpdateFlagRequest)(implicit
    encoder: Encoder[UpdateFlagRequest],
    decoder: Decoder[Flag]
  ): F[Flag] =
    put(s"$apiBasePath/$flagID", flagRequest)

  def restoreFlag(flagID: Long)(implicit decoder: Decoder[Flag]): F[Flag] =
    put(s"$apiBasePath/$flagID/restore")

  def enableFlag(flagID: Long, flagRequest: EnableFlagRequest)(implicit
    encoder: Encoder[EnableFlagRequest],
    decoder: Decoder[Flag]
  ): F[Flag] =
    put(s"$apiBasePath/$flagID/enabled", flagRequest)
}
