package crystailx.scalaflagr.client.sttp

import com.softwaremill.sttp.Method
import crystailx.scalaflagr.client.HttpMethod

class SttpHttpClientGetSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.GET
  override protected val specHttpMethod: HttpMethod = HttpMethod.Get

}
