package crystailx.scalaflagr.client.sttp

import com.softwaremill.sttp.Method
import crystailx.scalaflagr.client.HttpMethod

class SttpHttpClientPutSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.PUT
  override protected val specHttpMethod: HttpMethod = HttpMethod.Put

}
