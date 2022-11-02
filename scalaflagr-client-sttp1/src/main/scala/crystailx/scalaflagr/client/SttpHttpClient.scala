package crystailx.scalaflagr.client

import com.softwaremill.sttp._
import crystailx.scalaflagr.client.AuthMethod.{ Basic, Header }
import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.{ FlagrConfig, FlagrRequest }

import scala.concurrent.{ ExecutionContext, Future }

class SttpHttpClient(override protected val config: FlagrConfig)(implicit
  protected val sttpBackend: SttpBackend[Future, Nothing],
  protected val ec: ExecutionContext
) extends HttpClient[Future] {
  import config._

  override def send(request: FlagrRequest): Future[RawValue] = {
    import request._
    val uri = uri"$host/$basePath/$path".params(params)
    val sttpRequest = method match {
      case HttpMethod.Get    => sttp.get(uri)
      case HttpMethod.Delete => sttp.delete(uri)
      case HttpMethod.Post   => sttp.post(uri)
      case HttpMethod.Put    => sttp.put(uri)
      case HttpMethod.Patch  => sttp.patch(uri)
    }
    val withBody = body
      .fold(sttpRequest)(sttpRequest.body(_))
      .contentType(MediaTypes.Json)
    val withHeaders = withBody.headers(headers)
    val withAuth = authMethod match {
      case Some(Basic) =>
        basicAuth.fold(withHeaders)(b => withHeaders.auth.basic(b.username, b.password))
      case Some(Header(userId)) =>
        withHeaders.header(headerAuth.fold("X-Email")(_.field), userId, replaceExisting = true)
      case _ => withHeaders
    }
    withAuth
      .response(asByteArray)
      .send()
      .map(_.body.fold(s => throw new Exception(s), identity))
  }

}
