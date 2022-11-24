package crystailx.scalaflagr.client.sttp

import com.softwaremill.sttp.Method
import crystailx.scalaflagr.client.HttpMethod

class SttpHttpClientPatchSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.PATCH
  override protected val specHttpMethod: HttpMethod = HttpMethod.Patch

}
