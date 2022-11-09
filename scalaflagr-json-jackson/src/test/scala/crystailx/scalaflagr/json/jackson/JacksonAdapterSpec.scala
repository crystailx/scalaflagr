package crystailx.scalaflagr.json.jackson

import crystailx.scalaflagr.json.{ AdapterBase, Decoder, Encoder, TestData }

class JacksonAdapterSpec extends AdapterBase[Throwable] {
  override implicit val encoder: Encoder[TestData] = encoderAdapter
  override implicit val decoder: Decoder[TestData] = decoderAdapter
  override protected lazy val jsonLibraryName: String = "jackson"
}
