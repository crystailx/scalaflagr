package crystailx.scalaflagr.client.sttp

import com.softwaremill.sttp.Method
import crystailx.scalaflagr.client.HttpMethod

class SttpHttpClientDeleteSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.DELETE
  override protected val specHttpMethod: HttpMethod = HttpMethod.Delete

}
