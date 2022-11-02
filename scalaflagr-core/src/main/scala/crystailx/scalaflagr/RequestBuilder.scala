package crystailx.scalaflagr

import crystailx.scalaflagr.client.{ AuthMethod, HttpMethod }
import crystailx.scalaflagr.client.HttpMethod._
import crystailx.scalaflagr.json.Encoder

case class RequestBuilder[T](
  method: HttpMethod,
  path: String,
  body: Option[T] = None,
  params: Map[String, String] = Map.empty,
  headers: Map[String, String] = Map.empty,
  authMethod: Option[AuthMethod] = None
) {

  def withHeader(key: String, value: String): RequestBuilder[T] =
    copy(headers = headers + (key -> value))

  def withParam(key: String, value: String): RequestBuilder[T] =
    copy(params = params + (key -> value))

  def authHeader(userId: String): RequestBuilder[T] =
    copy(authMethod = Some(AuthMethod.Header(userId)))

  def authBasic: RequestBuilder[T] = copy(authMethod = Some(AuthMethod.Basic))

  def build(implicit encoder: Encoder[T]): FlagrRequest =
    FlagrRequest(method, path, body.map(encoder.encode), params, headers, authMethod)

}

object RequestBuilder {

  def get(url: String, params: Map[String, String] = Map.empty): RequestBuilder[Nothing] =
    RequestBuilder(Get, url, None, params)

  def delete(url: String, params: Map[String, String] = Map.empty): RequestBuilder[Nothing] =
    RequestBuilder(Delete, url, None, params)

  def post(url: String): RequestBuilder[Nothing] =
    RequestBuilder(Post, url, None, Map.empty)

  def post(url: String, params: Map[String, String]): RequestBuilder[Nothing] =
    RequestBuilder(Post, url, None, params)

  def post[T](url: String, body: T): RequestBuilder[T] =
    RequestBuilder(Post, url, Option(body), Map.empty)

  def post[T](url: String, body: T, params: Map[String, String]): RequestBuilder[T] =
    RequestBuilder(Post, url, Option(body), params)

  def put(url: String): RequestBuilder[Nothing] =
    RequestBuilder(Put, url, None, Map.empty)

  def put(url: String, params: Map[String, String]): RequestBuilder[Nothing] =
    RequestBuilder(Put, url, None, params)

  def put[T](url: String, body: T): RequestBuilder[T] =
    RequestBuilder(Put, url, Option(body), Map.empty)

  def put[T](url: String, body: T, params: Map[String, String]): RequestBuilder[T] =
    RequestBuilder(Put, url, Option(body), params)

  def patch(url: String): RequestBuilder[Nothing] =
    RequestBuilder(Patch, url, None, Map.empty)

  def patch(url: String, params: Map[String, String]): RequestBuilder[Nothing] =
    RequestBuilder(Patch, url, None, params)

  def patch[T](url: String, body: T): RequestBuilder[T] =
    RequestBuilder(Patch, url, Option(body), Map.empty)
}
