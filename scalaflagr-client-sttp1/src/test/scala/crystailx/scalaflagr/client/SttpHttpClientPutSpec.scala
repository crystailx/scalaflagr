package crystailx.scalaflagr.client

import com.softwaremill.sttp.Method

class SttpHttpClientPutSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.PUT
  override protected val specHttpMethod: HttpMethod = HttpMethod.Put

}
