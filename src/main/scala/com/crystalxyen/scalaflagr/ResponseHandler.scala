package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.OptionImplicits._
import com.crystalxyen.scalaflagr.client.{HttpEntity, HttpResponse}
import com.typesafe.scalalogging.LazyLogging
import io.circe.{Decoder, Json}

trait ResponseHandler[U] {

  /** Accepts a HttpResponse and returns an Either of an FlagrError or a type specific to the request
    * as determined by the instance of this handler.
    */
  def handle(response: HttpResponse)(
      implicit
      decoder: Decoder[U]
  ): Either[FlagrError, U]
}

// a ResponseHandler that marshalls the body into the required type using Jackson
// the response body is converted into a string using a codec derived from the content encoding header
// if the content encoding header is null, then UTF-8 is assumed
object ResponseHandler extends LazyLogging {

  def json(entity: HttpEntity.StringEntity): Json =
    fromEntity[Json](entity)

  def fromNode[U: Manifest: Decoder](node: Json): U = {
    logger.debug(
      s"Attempting to unmarshall json node to ${manifest.runtimeClass.getName}"
    )
    node.as[U].fold(throw _, identity)
  }

  def fromResponse[U: Manifest: Decoder](response: HttpResponse): U =
    fromEntity(response.entity.getOrError("No entity defined but was expected"))

  def fromEntity[U: Manifest: Decoder](entity: HttpEntity.StringEntity): U = {
    logger.debug(
      s"Attempting to unmarshall response to ${manifest.runtimeClass.getName}"
    )
    logger.debug(entity.content)
    io.circe.parser.decode[U](entity.content).fold(throw _, identity)
  }

  def default[U: Manifest] = new DefaultResponseHandler[U]
  def failure404[U: Manifest] = new NotFound404ResponseHandler[U]
}

// standard response handler, 200-204s are ok, and everything else is marhalled into an error
class DefaultResponseHandler[U: Manifest] extends ResponseHandler[U] {
  self =>

  override def handle(
      response: HttpResponse
  )(implicit decoder: Decoder[U]): Either[FlagrError, U] =
    response.statusCode match {
      case 200 | 201 | 202 | 203 | 204 =>
        val entity = response.entity.getOrError("No entity defined")
        Right(ResponseHandler.fromEntity[U](entity))
      case _ =>
        Left(FlagrError.parse(response))
    }

  def map[V](fn: U => V)(implicit decoderU: Decoder[U]): ResponseHandler[V] =
    new ResponseHandler[V] {

      override def handle(
          response: HttpResponse
      )(implicit decoder: Decoder[V]): Either[FlagrError, V] =
        self.handle(response)(decoderU) match {
          case Left(error) => Left(error)
          case Right(u)    => Right(fn(u))
        }
    }
}

class NotFound404ResponseHandler[U: Manifest]
    extends DefaultResponseHandler[U] {

  override def handle(
      response: HttpResponse
  )(implicit decoder: Decoder[U]): Either[FlagrError, U] =
    response.statusCode match {
      case 404 | 500 => sys.error(response.toString)
      case _         => super.handle(response)
    }
}
