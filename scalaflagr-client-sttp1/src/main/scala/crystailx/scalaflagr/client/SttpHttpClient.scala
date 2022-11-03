package crystailx.scalaflagr.client

import com.softwaremill.sttp._
import crystailx.scalaflagr.auth.{ BasicAuthConfig, HeaderIdentifierConfig }
import crystailx.scalaflagr.client.AuthMethod.{ Basic, JWT, NoAuth }
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
    val uri = uri"${s"$host$basePath$path"}".params(params)
    val sttpRequest = method match {
      case HttpMethod.Get    => sttp.get(uri)
      case HttpMethod.Delete => sttp.delete(uri)
      case HttpMethod.Post   => sttp.post(uri)
      case HttpMethod.Put    => sttp.put(uri)
      case HttpMethod.Patch  => sttp.patch(uri)
    }
    import SttpHttpClient.RichSttpRequest
    sttpRequest
      .withBody(body)
      .withHeaders(headers)
      .withGlobalAuth(config)
      .withAuth(authMethod)
      .withIdentifier(identifier -> config.headerIdentifier)
      .response(asByteArray)
      .send()
      .map(_.body.fold(s => throw new Exception(s), identity))
  }

}

object SttpHttpClient {

  implicit class RichSttpRequest(val sttpRequest: Request[String, Nothing]) extends AnyVal {

    def withBody(body: Option[RawValue]): Request[String, Nothing] = body
      .fold(sttpRequest)(sttpRequest.body(_))
      .contentType(MediaTypes.Json)

    def withHeaders(headers: Map[String, String]): Request[String, Nothing] =
      sttpRequest.headers(headers)

    def withGlobalAuth(config: FlagrConfig): Request[String, Nothing] = config match {
      case FlagrConfig(_, _, _, Some(BasicAuthConfig(username, password))) =>
        sttpRequest.auth.basic(username, password)
      case _ => sttpRequest
    }

    def withAuth(authMethod: Option[AuthMethod]): Request[String, Nothing] =
      authMethod match {
        case Some(Basic(username, password)) =>
          sttpRequest.auth.basic(username, password)
        case Some(JWT(_)) => throw new Exception("Unsupported authentication method")
        case Some(NoAuth) =>
          sttpRequest.headers(
            sttpRequest.headers
              .filterNot(_._1.equalsIgnoreCase(HeaderNames.Authorization)): _*
          )
        case _ => sttpRequest
      }

    def withIdentifier(
      identifiers: (Option[Identifier], Option[HeaderIdentifierConfig])
    ): Request[String, Nothing] = identifiers match {
      case (Some(Identifier(userId, Some(field))), _) => sttpRequest.header(field, userId)
      case (Some(Identifier(userId, None)), Some(HeaderIdentifierConfig(field, _))) =>
        sttpRequest.header(field, userId)
      case (Some(Identifier(userId, None)), None) =>
        sttpRequest.header("X-Email", userId)
      case (None, Some(HeaderIdentifierConfig(field, Some(userId)))) =>
        sttpRequest.header(field, userId)
      case _ => sttpRequest
    }
  }
}
