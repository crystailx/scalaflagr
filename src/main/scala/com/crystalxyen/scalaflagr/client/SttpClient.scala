package com.crystalxyen.scalaflagr.client

import com.crystalxyen.scalaflagr.OptionImplicits._
import com.crystalxyen.scalaflagr.client.HttpEntity.{
  ByteArrayEntity,
  FileEntity,
  InputStreamEntity,
  StringEntity
}
import com.crystalxyen.scalaflagr.{FlagrEndpoint, FlagrRequest}
import com.softwaremill.sttp.Uri.QueryFragment
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend

import java.io._
import java.nio.file.Files
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success}

class SttpClient(nodeEndpoint: FlagrEndpoint)(
    implicit
    ec: ExecutionContext,
    sttpBackend: SttpBackend[Future, Nothing]
) extends HttpClient {

  private def request(
      method: String,
      endpoint: String,
      params: Map[String, Any],
      headers: Map[String, String]
  ): Request[String, Nothing] = {
    val url = new Uri(
      nodeEndpoint.protocol,
      None,
      nodeEndpoint.host,
      nodeEndpoint.port.some,
      collection.immutable.Seq(endpoint.stripPrefix("/").split('/'): _*),
      collection.immutable.Seq(params.map {
        case (k, v) =>
          QueryFragment.KeyValue(k, v.toString)
      }.toSeq: _*),
      None
    )
    val req = method.toUpperCase match {
      case "GET"    => sttp.get(url)
      case "HEAD"   => sttp.head(url)
      case "POST"   => sttp.post(url)
      case "PUT"    => sttp.put(url)
      case "DELETE" => sttp.delete(url)
    }
    req.headers(headers)
  }

  private def processResponse(resp: Response[String]): HttpResponse = {
    val strToHttpEntity = (body: String) =>
      HttpEntity.StringEntity(body, resp.contentType).some
    val entity = resp.body.fold(
      strToHttpEntity,
      strToHttpEntity
    )

    HttpResponse(resp.code, entity, resp.headers.toMap)
  }

  def async(
      method: String,
      endpoint: String,
      params: Map[String, Any],
      headers: Map[String, String]
  ): Request[String, Nothing] =
    request(method, endpoint, params, headers)

  def async(
      method: String,
      endpoint: String,
      params: Map[String, Any],
      headers: Map[String, String],
      entity: HttpEntity
  ): Request[String, Nothing] = {
    val r = request(method, endpoint, params, headers)
    val r2 = entity.contentCharset.fold(r)(r.contentType)
    entity match {
      case StringEntity(content: String, _) => r2.body(content)
      case ByteArrayEntity(content, _)      => r2.body(content)
      case InputStreamEntity(in: InputStream, _) =>
        r2.body(Source.fromInputStream(in, "UTF8").getLines().mkString("\n"))
      case FileEntity(file: File, _) => r2.body(Files.readAllBytes(file.toPath))
    }
  }

  override def close(): Unit = sttpBackend.close()

  /** Sends the given request to elasticsearch.
    *
    * Implementations should invoke the callback function once the response is known.
    *
    * The callback function should be invoked with a HttpResponse for all requests that received
    * a response, including 4xx and 5xx responses. The callback function should only be invoked
    * with an exception if the client failed.
    */
  override def send(
      request: FlagrRequest,
      callback: Either[Throwable, HttpResponse] => Unit
  ): Unit = {
    val f = request.entity match {
      case Some(entity) =>
        async(
          request.method,
          request.endpoint,
          request.params,
          request.headers,
          entity
        ).send()
      case None =>
        async(request.method, request.endpoint, request.params, request.headers)
          .send()
    }
    f.onComplete {
      case Success(resp) => callback(Right(processResponse(resp)))
      case Failure(t)    => callback(Left(t))
    }
  }
}

object SttpClient {

  private def defaultEc: ExecutionContext = ExecutionContext.global

  private def defaultSttpBackend: SttpBackend[Future, Nothing] =
    AkkaHttpBackend(
      options = SttpBackendOptions.connectionTimeout(1.minute)
    )

  /** Instantiate an [[SttpClient]] with reasonable defaults for the implicit parameters. */
  def apply(nodeEndpoint: FlagrEndpoint): SttpClient =
    new SttpClient(nodeEndpoint)(defaultEc, defaultSttpBackend)
}
