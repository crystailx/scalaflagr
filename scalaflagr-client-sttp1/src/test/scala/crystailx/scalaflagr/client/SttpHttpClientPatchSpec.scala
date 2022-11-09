package crystailx.scalaflagr.client

import com.softwaremill.sttp.Method

class SttpHttpClientPatchSpec extends SttpHttpClientFixtureBase {
  override protected lazy val expectedHttpMethod: Method = Method.PATCH
  override protected val specHttpMethod: HttpMethod = HttpMethod.Patch

}
