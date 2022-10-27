package io.github.crystailx.scalaflagr.client

import com.softwaremill.sttp._
import io.github.crystailx.scalaflagr.effect.Functor

import scala.concurrent.{ ExecutionContext, Future }

abstract class SttpHttpClient extends HttpClient[Future] {

  implicit protected val sttpBackend: SttpBackend[Future, Nothing]
  implicit protected val ec: ExecutionContext

  override protected def send(
    method: HttpMethod,
    url: String,
    body: Option[String],
    params: Map[String, String]
  ): Future[String] = {
    val uri = uri"$url".params(params)
    val request = method match {
      case HttpMethod.Get    => sttp.get(uri)
      case HttpMethod.Delete => sttp.delete(uri)
      case HttpMethod.Post   => sttp.post(uri)
      case HttpMethod.Put    => sttp.put(uri)
      case HttpMethod.Patch  => sttp.patch(uri)
    }
    body
      .fold(request)(request.body(_))
      .contentType(MediaTypes.Json)
      .response(asString)
      .send()
      .map(_.body.fold(s => throw new Exception(s), identity))
  }

}
