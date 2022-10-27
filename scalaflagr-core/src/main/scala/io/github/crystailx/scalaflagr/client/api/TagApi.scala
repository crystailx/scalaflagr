package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.implicitConversions

trait TagApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig

  import config._

  private val apiBasePath: String = s"$host$basePath/flags/%d/tags"
  private val globalTagApiBasePath: String = s"$host$basePath/tags"

  def createTag(flagID: Long, body: CreateTagRequest)(implicit
    encoder: Encoder[CreateTagRequest],
    decoder: Decoder[Tag]
  ): F[Tag] =
    post(apiBasePath format flagID, body)

  def deleteTag(flagID: Long, tagID: Long): F[Unit] =
    delete(s"$apiBasePath/$tagID" format flagID)

  def findTags(param: FindTagsParam)(implicit decoder: Decoder[List[Tag]]): F[List[Tag]] =
    get(globalTagApiBasePath, buildParamsMap(param))

  def flagTags(flagID: Long)(implicit decoder: Decoder[List[Tag]]): F[List[Tag]] =
    get(apiBasePath format flagID)
}
