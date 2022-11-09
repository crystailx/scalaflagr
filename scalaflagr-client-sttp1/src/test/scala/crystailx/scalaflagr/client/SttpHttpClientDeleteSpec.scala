package crystailx.scalaflagr.client

import com.softwaremill.sttp.Method

class SttpHttpClientDeleteSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.DELETE
  override protected val specHttpMethod: HttpMethod = HttpMethod.Delete

}
