package crystailx.scalaflagr

import crystailx.scalaflagr.client.HttpMethod._
import crystailx.scalaflagr.client.{ AuthMethod, HttpMethod, Identifier }
import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.json.{ Decoder, Encoder }

case class RequestHandler[T, R](
  method: HttpMethod,
  path: String,
  body: Option[T] = None,
  params: Map[String, String] = Map.empty,
  headers: Map[String, String] = Map.empty,
  identifier: Option[Identifier] = None,
  authMethod: Option[AuthMethod] = None
) {

  def withHeader(key: String, value: String): RequestHandler[T, R] =
    copy(headers = headers + (key -> value))

  def withParam(key: String, value: String): RequestHandler[T, R] =
    copy(params = params + (key -> value))

  def identifier(userId: String, field: Option[String] = None): RequestHandler[T, R] =
    copy(identifier = Some(Identifier(userId, field)))

  def authBasic(username: String, password: String): RequestHandler[T, R] =
    copy(authMethod = Some(AuthMethod.Basic(username, password)))

  def noAuth: RequestHandler[T, R] = copy(authMethod = Some(AuthMethod.NoAuth))

  def build(implicit encoder: Encoder[T]): FlagrRequest =
    FlagrRequest(method, path, body.map(encoder.encode), params, headers, identifier, authMethod)

  def handle(rawResponse: RawValue)(implicit decoder: Decoder[R]): Either[Throwable, R] =
    decoder.decodeSafe(rawResponse)

}

object RequestHandler {

  def get[R](url: String, params: Map[String, String] = Map.empty): RequestHandler[Nothing, R] =
    RequestHandler(Get, url, None, params)

  def delete[R](url: String, params: Map[String, String] = Map.empty): RequestHandler[Nothing, R] =
    RequestHandler(Delete, url, None, params)

  def post[R](url: String): RequestHandler[Nothing, R] =
    RequestHandler(Post, url, None, Map.empty)

  def post[R](url: String, params: Map[String, String]): RequestHandler[Nothing, R] =
    RequestHandler(Post, url, None, params)

  def post[T, R](url: String, body: T): RequestHandler[T, R] =
    RequestHandler(Post, url, Option(body), Map.empty)

  def post[T, R](url: String, body: T, params: Map[String, String]): RequestHandler[T, R] =
    RequestHandler(Post, url, Option(body), params)

  def put[R](url: String): RequestHandler[Nothing, R] =
    RequestHandler(Put, url, None, Map.empty)

  def put[R](url: String, params: Map[String, String]): RequestHandler[Nothing, R] =
    RequestHandler(Put, url, None, params)

  def put[T, R](url: String, body: T): RequestHandler[T, R] =
    RequestHandler(Put, url, Option(body), Map.empty)

  def put[T, R](url: String, body: T, params: Map[String, String]): RequestHandler[T, R] =
    RequestHandler(Put, url, Option(body), params)

  def patch[R](url: String): RequestHandler[Nothing, R] =
    RequestHandler(Patch, url, None, Map.empty)

  def patch[R](url: String, params: Map[String, String]): RequestHandler[Nothing, R] =
    RequestHandler(Patch, url, None, params)

  def patch[T, R](url: String, body: T): RequestHandler[T, R] =
    RequestHandler(Patch, url, Option(body), Map.empty)
}
