package crystailx.scalaflagr.client

import com.softwaremill.sttp
import com.softwaremill.sttp.{
  ByteArrayBody,
  HeaderNames,
  Method,
  NoBody,
  Request,
  Response,
  StatusCodes,
  SttpBackend
}
import crystailx.scalaflagr.auth.{ BasicAuthConfig, HeaderIdentifierConfig }
import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.{ FlagrConfig, FlagrRequest }
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{ Assertion, Outcome }

import java.util.Base64
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

trait SttpHttpClientFixtureBase extends FixtureAnyFlatSpec with Matchers with MockFactory {

  case class SttpBackendClient(client: SttpHttpClient, backend: SttpBackend[Future, Nothing]) {

    def backendSend: Request[RawValue, Nothing] => Future[Response[RawValue]] =
      backend.send[RawValue]
    def clientSend(request: FlagrRequest): Future[RawValue] = client.send(request)
  }

  protected val expectedHttpMethod: Method
  protected val specHttpMethod: HttpMethod

  override protected def withFixture(test: OneArgTest): Outcome = {
    val mockedBackend = mock[SttpBackend[Future, Nothing]]
    test(SttpBackendClient(new SttpHttpClient(FlagrConfig())(mockedBackend, global), mockedBackend))
  }

  override type FixtureParam = SttpBackendClient

  type RequestType = Request[RawValue, Nothing]
  type RequestMatcher = RequestType => Boolean

  val testRawBody: RawValue = "{}".getBytes
  val testExtraHeader: (String, String) = "x-test-header" -> "test value"

  val matchesMethod: RequestMatcher = _.method == expectedHttpMethod

  val matchesErrorPath: RequestMatcher = _.uri.path == List("api", "v1", "test", "error")

  val matchesBuilderPath: RequestMatcher = _.uri.path == List("api", "v1", "test", "builder")

  val noBodyExists: RequestMatcher = _.body == NoBody

  val testBodyExists: RequestMatcher = _.body match {
    case body: ByteArrayBody => body.b sameElements testRawBody
    case _                   => false
  }

  val hasJsonContentType: RequestMatcher =
    _.headers.toSet.contains(HeaderNames.ContentType, "application/json")

  val noExtraHeaders: RequestMatcher = !_.headers.toSet.contains(testExtraHeader)

  val hasExtraHeaders: RequestMatcher =
    _.headers.toSet.contains(testExtraHeader._1, testExtraHeader._2)

  def hasBasicAuth(username: String, password: String): RequestMatcher = {
    val token = Base64.getEncoder.encodeToString(s"$username:$password".getBytes)
    _.headers.toSet.contains("Authorization", s"Basic $token")
  }

  def hasIdentifier(field: String, user: String): RequestMatcher =
    _.headers.toSet.contains(field, user)

  implicit class RichRequestMatch(matcher: RequestMatcher) {

    def and(anotherMatcher: RequestMatcher): RequestMatcher = req =>
      matcher(req) && anotherMatcher(req)
  }

  def testErrorResponse(fp: SttpBackendClient, statusCode: sttp.StatusCode): Assertion = {
    val SttpBackendClient(client, backend) = fp
    backend.send[RawValue] _ expects where(matchesMethod and matchesErrorPath) returning Future(
      Response.error(s"error: $statusCode", statusCode)
    )
    the[Exception] thrownBy Await.result(
      client.send(FlagrRequest(specHttpMethod, "/test/error")),
      Duration.Inf
    ) must have message s"error: $statusCode"
  }

  behavior of "error response handling"

  it must s"throw an exception on bad request status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.BadRequest)
  }

  it must s"throw an exception on unauthorized status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.Unauthorized)
  }

  it must s"throw an exception on forbidden status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.Forbidden)
  }

  it must s"throw an exception on not found status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.NotFound)
  }

  it must s"throw an exception on too many requests status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.TooManyRequests)
  }

  it must s"throw an exception on internal server error status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.InternalServerError)
  }

  it must s"throw an exception on bad gateway status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.BadGateway)
  }

  it must s"throw an exception on service unavailable status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.ServiceUnavailable)
  }

  it must s"throw an exception on gateway timeout status code using ${expectedHttpMethod.m} method" in {
    testErrorResponse(_, StatusCodes.GatewayTimeout)
  }

  behavior of "request builder"

  def extractor(fp: SttpBackendClient)(
    oneTest: (SttpHttpClient, SttpBackend[Future, Nothing]) => Unit
  ): Unit = oneTest(fp.client, fp.backend)

  it must s"build request without body using ${expectedHttpMethod.m} method" in { fp =>
    val SttpBackendClient(client, backend) = fp
    backend.send[RawValue] _ expects where {
      matchesMethod and matchesBuilderPath and hasJsonContentType and noBodyExists
    } returning Future(Response.ok(testRawBody))

    noException must be thrownBy client.send(
      FlagrRequest(specHttpMethod, "/test/builder", None)
    )
  }

  it must s"build request with body using ${expectedHttpMethod.m} method" in { fp =>
    val SttpBackendClient(client, backend) = fp
    backend.send[RawValue] _ expects where {
      matchesMethod and matchesBuilderPath and hasJsonContentType and testBodyExists
    } returning Future(Response.ok(testRawBody))
    noException must be thrownBy client.send(
      FlagrRequest(specHttpMethod, "/test/builder", Some(testRawBody))
    )
  }

  it must s"build request without extra headers using ${expectedHttpMethod.m} method" in { fp =>
    val SttpBackendClient(client, backend) = fp
    backend.send[RawValue] _ expects where {
      matchesMethod and matchesBuilderPath and noExtraHeaders
    } returning Future(Response.ok(testRawBody))
    noException must be thrownBy client.send(
      FlagrRequest(specHttpMethod, "/test/builder", headers = Map.empty)
    )
  }

  it must s"build request with extra headers using ${expectedHttpMethod.m} method" in { fp =>
    val SttpBackendClient(client, backend) = fp
    backend.send[RawValue] _ expects where {
      matchesMethod and matchesBuilderPath and hasExtraHeaders
    } returning Future(Response.ok(testRawBody))
    noException must be thrownBy client.send(
      FlagrRequest(specHttpMethod, "/test/builder", headers = Map(testExtraHeader))
    )
  }

  it must s"build request with basic auth headers using ${expectedHttpMethod.m} method when global setting exists" in {
    fp =>
      val username = "test-user"
      val password = "1234567890"
      implicit val backend: SttpBackend[Future, Nothing] = fp.backend
      val client =
        new SttpHttpClient(FlagrConfig(basicAuth = Some(BasicAuthConfig(username, password))))
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasBasicAuth(username, password)
      } returning Future(Response.ok(testRawBody))

      noException must be thrownBy client.send(
        FlagrRequest(specHttpMethod, "/test/builder")
      )
  }

  it must s"build request with basic auth headers provided in request using ${expectedHttpMethod.m} method even when global setting exists" in {
    fp =>
      val username = "test-user"
      val password = "1234567890"
      implicit val backend: SttpBackend[Future, Nothing] = fp.backend
      val client =
        new SttpHttpClient(FlagrConfig(basicAuth = Some(BasicAuthConfig("default", "default"))))
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasBasicAuth(username, password)
      } returning Future(Response.ok(testRawBody))

      noException must be thrownBy client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          authMethod = Some(AuthMethod.Basic(username, password))
        )
      )
  }

  it must s"build request with basic auth headers provided in request using ${expectedHttpMethod.m} method no global setting exists" in {
    fp =>
      val username = "test-user"
      val password = "1234567890"
      val SttpBackendClient(client, backend) = fp
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasBasicAuth(username, password)
      } returning Future(Response.ok(testRawBody))

      noException must be thrownBy client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          authMethod = Some(AuthMethod.Basic(username, password))
        )
      )
  }

  it must s"throw an exception when JWT auth methods provided in request using ${expectedHttpMethod.m} method" in {
    fp =>
      an[Exception] must be thrownBy fp.client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          authMethod = Some(AuthMethod.JWT("token"))
        )
      )
  }

  it must s"build request with no authorization headers when NoAuth method is provided in request using ${expectedHttpMethod.m} method" in {
    fp =>
      an[Exception] must be thrownBy fp.client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          headers = Map("Authorization" -> "Bearer 123"),
          authMethod = Some(AuthMethod.NoAuth)
        )
      )
  }

  it must s"build request with identifiers in request using ${expectedHttpMethod.m} method when global setting exists" in {
    fp =>
      val user = "test-user"
      val field = "X-User"
      implicit val backend: SttpBackend[Future, Nothing] = fp.backend
      val client =
        new SttpHttpClient(
          FlagrConfig(headerIdentifier = Some(HeaderIdentifierConfig(field, Some(user))))
        )
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasIdentifier(field, user)
      } returning Future(Response.ok(testRawBody))
      noException must be thrownBy client.send(FlagrRequest(specHttpMethod, "/test/builder"))
  }

  it must s"build request with identifiers provided in request using ${expectedHttpMethod.m} method even when global setting exists" in {
    fp =>
      val user = "test-user"
      val field = "X-User"
      implicit val backend: SttpBackend[Future, Nothing] = fp.backend
      val client =
        new SttpHttpClient(
          FlagrConfig(headerIdentifier = Some(HeaderIdentifierConfig("default", Some("default"))))
        )
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasIdentifier(field, user)
      } returning Future(Response.ok(testRawBody))
      noException must be thrownBy client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          identifier = Some(Identifier(user, Some(field)))
        )
      )
  }

  it must s"build request with identifier provided in request and global identifier field using ${expectedHttpMethod.m} method" in {
    fp =>
      val user = "test-user"
      val field = "X-User"
      implicit val backend: SttpBackend[Future, Nothing] = fp.backend
      val client =
        new SttpHttpClient(
          FlagrConfig(headerIdentifier = Some(HeaderIdentifierConfig(field, None)))
        )
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasIdentifier(field, user)
      } returning Future(Response.ok(testRawBody))
      noException must be thrownBy client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          identifier = Some(Identifier(user))
        )
      )
  }

  it must s"build request with identifiers provided in request using ${expectedHttpMethod.m} method when no global setting exists" in {
    fp =>
      val user = "test-user"
      val field = "X-User"
      val SttpBackendClient(client, backend) = fp
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasIdentifier(field, user)
      } returning Future(Response.ok(testRawBody))
      noException must be thrownBy client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          identifier = Some(Identifier(user, Some(field)))
        )
      )
  }

  it must s"build request with identifier provided in request and default field using ${expectedHttpMethod.m} method" in {
    fp =>
      val user = "test-user"
      val SttpBackendClient(client, backend) = fp
      backend.send[RawValue] _ expects where {
        matchesMethod and matchesBuilderPath and hasIdentifier("X-Email", user)
      } returning Future(Response.ok(testRawBody))
      noException must be thrownBy client.send(
        FlagrRequest(
          specHttpMethod,
          "/test/builder",
          identifier = Some(Identifier(user, None))
        )
      )
  }

}
