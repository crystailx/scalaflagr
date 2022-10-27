package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.implicitConversions

trait VariantApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig

  import config._

  private val apiBasePath: String = s"$host$basePath/flags/%d/variants"

  def createVariant(flagID: Long, body: CreateVariantRequest)(implicit
    encoder: Encoder[CreateVariantRequest],
    decoder: Decoder[Variant]
  ): F[Variant] =
    post(apiBasePath format flagID, body)

  def deleteVariant(flagID: Long, variantID: Long): F[Unit] =
    delete(s"$apiBasePath/$variantID" format flagID)

  def flagVariants(flagID: Long)(implicit decoder: Decoder[List[Variant]]): F[List[Variant]] =
    get(apiBasePath format flagID)

  def updateVariant(flagID: Long, variantID: Long, body: UpdateVariantRequest)(implicit
    encoder: Encoder[UpdateVariantRequest],
    decoder: Decoder[Variant]
  ): F[Variant] =
    put(s"$apiBasePath/$variantID" format flagID, body)
}
