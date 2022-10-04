package com.crystalxyen.scalaflagr

import com.typesafe.scalalogging.LazyLogging

/** A [[Handler]] is a typeclass used by the [[ElasticClient]] in order to
  * create [[ElasticRequest]] instances which are sent to the elasticsearch
  * server, as well as returning a [[ResponseHandler]] which handles the
  * response from the server.
  *
  * @tparam T the type of the request object handled by this handler
  * @tparam U the type of the response object returned by this handler
  */
abstract class Handler[T, U: Manifest] extends LazyLogging {
  val basePath: String = "/api/v1"
  def responseHandler: ResponseHandler[U] = ResponseHandler.default[U]
  def build(t: T): FlagrRequest
}
