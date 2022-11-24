package crystailx.scalaflagr.client.sttp

import com.softwaremill.sttp.Method
import crystailx.scalaflagr.client.HttpMethod

class SttpHttpClientPostSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.POST
  override protected val specHttpMethod: HttpMethod = HttpMethod.Post

}
