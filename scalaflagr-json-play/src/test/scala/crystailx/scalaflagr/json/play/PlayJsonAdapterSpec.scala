package crystailx.scalaflagr.json.play

import crystailx.scalaflagr.json.{ AdapterBase, Decoder, Encoder, TestData }
import play.api.libs.json.{ JsResultException, Json, Reads => PlayDecoder, Writes => PlayEncoder }

class PlayJsonAdapterSpec extends AdapterBase[JsResultException] {
  implicit val testDataEncoder: PlayEncoder[TestData] = Json.writes
  implicit val testDataDecoder: PlayDecoder[TestData] = Json.reads
  override val encoder: Encoder[TestData] = implicitly
  override val decoder: Decoder[TestData] = implicitly
}
