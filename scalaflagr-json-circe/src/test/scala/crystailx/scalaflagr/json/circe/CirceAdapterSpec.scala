package crystailx.scalaflagr.json.circe

import crystailx.scalaflagr.json.{ AdapterBase, Decoder, Encoder, TestData }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ DecodingFailure, Decoder => CirceDecoder, Encoder => CirceEncoder }

class CirceAdapterSpec extends AdapterBase[DecodingFailure] {
  implicit val testDataEncoder: CirceEncoder[TestData] = deriveEncoder
  implicit val testDataDecoder: CirceDecoder[TestData] = deriveDecoder
  override val encoder: Encoder[TestData] = implicitly
  override val decoder: Decoder[TestData] = implicitly
}
