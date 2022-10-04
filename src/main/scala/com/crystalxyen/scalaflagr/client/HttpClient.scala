package com.crystalxyen.scalaflagr.client

import com.crystalxyen.scalaflagr.FlagrRequest
import com.typesafe.scalalogging.LazyLogging

import java.io.{File, InputStream}
import java.nio.file.Files
import scala.io.Source

trait HttpClient extends LazyLogging {

  /** Sends the given request to flagr server.
    *
    * Implementations should invoke the callback function once the response is known.
    *
    * The callback function should be invoked with a HttpResponse for all requests that received
    * a response, including 4xx and 5xx responses. The callback function should only be invoked
    * with an exception if the client could not complete the request.
    */
  def send(
      request: FlagrRequest,
      callback: Either[Throwable, HttpResponse] => Unit
  ): Unit

  /** Closes the underlying http client. Can be a no-op if the underlying client does not have
    * state that needs to be closed.
    */
  def close(): Unit
}

case class HttpResponse(
    statusCode: Int,
    entity: Option[HttpEntity.StringEntity],
    headers: Map[String, String]
)

sealed trait HttpEntity {
  def contentCharset: Option[String]
  def get: String
}

object HttpEntity {

  def apply(content: String): HttpEntity =
    HttpEntity(content, "application/json; charset=utf-8")

  def apply(content: String, contentType: String): HttpEntity =
    StringEntity(content, Some(contentType))

  case class StringEntity(content: String, contentCharset: Option[String])
      extends HttpEntity {
    def get: String = content
  }

  case class InputStreamEntity(
      content: InputStream,
      contentCharset: Option[String]
  ) extends HttpEntity {
    def get: String = Source.fromInputStream(content).getLines().mkString("\n")
  }

  case class FileEntity(content: File, contentCharset: Option[String])
      extends HttpEntity {

    import scala.collection.JavaConverters._

    def get: String = Files.readAllLines(content.toPath).asScala.mkString("\n")
  }

  case class ByteArrayEntity(
      content: Array[Byte],
      contentCharset: Option[String]
  ) extends HttpEntity {
    def get: String = new String(content, contentCharset.getOrElse("utf-8"))
  }
}
