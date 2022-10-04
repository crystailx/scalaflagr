package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.client.HttpClient
import com.typesafe.scalalogging.LazyLogging
import io.circe.Decoder

import scala.concurrent.duration._
import scala.language.higherKinds

case class FlagrClient(client: HttpClient) extends LazyLogging {

  /** Returns a String containing the request details.
    * The string will have the HTTP method, endpoint, params and if applicable the request body.
    */
  def show[T](t: T)(implicit handler: Handler[T, _]): String =
    Show[FlagrRequest].show(handler.build(t))

  // Executes the given request type T, and returns an effect of Response[U]
  // where U is particular to the request type.
  // For example a search request will return a Response[SearchResponse].
  def execute[T, U, F[_]](t: T)(
      implicit
      executor: Executor[F],
      functor: Functor[F],
      handler: Handler[T, U],
      manifest: Manifest[U],
      options: CommonRequestOptions,
      decoder: Decoder[U]
  ): F[Response[U]] = {
    val request = handler.build(t)
    val f = executor.exec(client, request)
    functor.map(f) { resp =>
      handler.responseHandler.handle(resp) match {
        case Right(u) =>
          RequestSuccess(
            resp.statusCode,
            resp.entity.map(_.content),
            resp.headers,
            u
          )
        case Left(error) =>
          RequestFailure(
            resp.statusCode,
            resp.entity.map(_.content),
            resp.headers,
            error
          )
      }
    }
  }

  def close(): Unit = client.close()
}
case class CommonRequestOptions(timeout: Duration, masterNodeTimeout: Duration)

object CommonRequestOptions {

  implicit val defaults: CommonRequestOptions =
    CommonRequestOptions(0.seconds, 0.seconds)
}
