package crystailx.scalaflagr.client

import com.softwaremill.sttp.Method

class SttpHttpClientPostSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.POST
  override protected val specHttpMethod: HttpMethod = HttpMethod.Post

}
