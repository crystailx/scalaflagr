package crystailx.scalaflagr.client

import com.softwaremill.sttp.Method

class SttpHttpClientGetSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.GET
  override protected val specHttpMethod: HttpMethod = HttpMethod.Get

}
