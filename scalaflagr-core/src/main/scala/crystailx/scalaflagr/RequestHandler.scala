package crystailx.scalaflagr

import crystailx.scalaflagr.client.HttpMethod._
import crystailx.scalaflagr.client.{ AuthMethod, HttpMethod, Identifier }
import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.json.{ Decoder, Encoder }

sealed trait RequestHandler[R] {
  val method: HttpMethod
  val path: String
  val params: Map[String, String]
  val headers: Map[String, String]
  val identifier: Option[Identifier]
  val authMethod: Option[AuthMethod]

  def withHeader(key: String, value: String): RequestHandler[R]

  def withParam(key: String, value: String): RequestHandler[R]

  def identifier(userId: String, field: Option[String] = None): RequestHandler[R]

  def authBasic(username: String, password: String): RequestHandler[R]

  def noAuth: RequestHandler[R]

  def build: FlagrRequest =
    FlagrRequest(method, path, None, params, headers, identifier, authMethod)

  def handle(rawResponse: RawValue)(implicit decoder: Decoder[R]): Either[Throwable, R] =
    decoder.decodeSafe(rawResponse)
}

case class NoBodyRequestHandler[R](
  method: HttpMethod,
  path: String,
  params: Map[String, String] = Map.empty,
  headers: Map[String, String] = Map.empty,
  identifier: Option[Identifier] = None,
  authMethod: Option[AuthMethod] = None
) extends RequestHandler[R] {

  def withHeader(key: String, value: String): NoBodyRequestHandler[R] =
    copy(headers = headers + (key -> value))

  def withParam(key: String, value: String): NoBodyRequestHandler[R] =
    copy(params = params + (key -> value))

  def identifier(userId: String, field: Option[String] = None): NoBodyRequestHandler[R] =
    copy(identifier = Some(Identifier(userId, field)))

  def authBasic(username: String, password: String): NoBodyRequestHandler[R] =
    copy(authMethod = Some(AuthMethod.Basic(username, password)))

  def noAuth: NoBodyRequestHandler[R] = copy(authMethod = Some(AuthMethod.NoAuth))

}

case class BodyRequestHandler[T, R](
  method: HttpMethod,
  path: String,
  body: T,
  params: Map[String, String] = Map.empty,
  headers: Map[String, String] = Map.empty,
  identifier: Option[Identifier] = None,
  authMethod: Option[AuthMethod] = None
) extends RequestHandler[R] {

  def withHeader(key: String, value: String): BodyRequestHandler[T, R] =
    copy(headers = headers + (key -> value))

  def withParam(key: String, value: String): BodyRequestHandler[T, R] =
    copy(params = params + (key -> value))

  def identifier(userId: String, field: Option[String] = None): BodyRequestHandler[T, R] =
    copy(identifier = Some(Identifier(userId, field)))

  def authBasic(username: String, password: String): BodyRequestHandler[T, R] =
    copy(authMethod = Some(AuthMethod.Basic(username, password)))

  def noAuth: BodyRequestHandler[T, R] = copy(authMethod = Some(AuthMethod.NoAuth))

  def build(implicit encoder: Encoder[T]): FlagrRequest =
    FlagrRequest(method, path, Some(encoder.encode(body)), params, headers, identifier, authMethod)

}

object RequestHandler {

  def get[R](url: String, params: Map[String, String] = Map.empty): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Get, url, params)

  def delete[R](url: String, params: Map[String, String] = Map.empty): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Delete, url, params)

  def post[R](url: String): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Post, url, Map.empty)

  def post[R](url: String, params: Map[String, String]): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Post, url, params)

  def post[T, R](url: String, body: T): BodyRequestHandler[T, R] =
    BodyRequestHandler(Post, url, body, Map.empty)

  def post[T, R](url: String, body: T, params: Map[String, String]): BodyRequestHandler[T, R] =
    BodyRequestHandler(Post, url, body, params)

  def put[R](url: String): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Put, url, Map.empty)

  def put[R](url: String, params: Map[String, String]): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Put, url, params)

  def put[T, R](url: String, body: T): BodyRequestHandler[T, R] =
    BodyRequestHandler(Put, url, body, Map.empty)

  def put[T, R](url: String, body: T, params: Map[String, String]): BodyRequestHandler[T, R] =
    BodyRequestHandler(Put, url, body, params)

  def patch[R](url: String): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Patch, url, Map.empty)

  def patch[R](url: String, params: Map[String, String]): NoBodyRequestHandler[R] =
    NoBodyRequestHandler(Patch, url, params)

  def patch[T, R](url: String, body: T): BodyRequestHandler[T, R] =
    BodyRequestHandler(Patch, url, body, Map.empty)
}
