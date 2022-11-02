package crystailx.scalaflagr.json.circe

import com.typesafe.scalalogging.LazyLogging
import crystailx.scalaflagr.json.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveCodec, deriveDecoder, deriveEncoder }
import io.circe.{
  DecodingFailure,
  Codec => CirceCodec,
  Decoder => CirceDecoder,
  Encoder => CirceEncoder
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.runtime.Nothing$

class CirceAdapterSpec extends AnyFlatSpec with LazyLogging with Matchers {

  case class TestData(name: String, age: Option[Int])

  it must "encode using circe encoders" in {
    val data = TestData("Tester", Some(30))
    implicit val encoder: CirceEncoder[TestData] = deriveEncoder
    val encoded = implicitly[Encoder[TestData]].encode(data)
    new String(encoded) mustBe """{"name":"Tester","age":30}"""
  }

  it must "decode using circe decoders" in {
    val data = """{"name":"Flagr"}""".getBytes
    implicit val decoder: CirceDecoder[TestData] = deriveDecoder
    val decoded = implicitly[Decoder[TestData]].decode(data)
    decoded mustBe TestData("Flagr", None)
  }

  it must "decode safely using circe decoders" in {
    val data = """{"invalid":"data"}""".getBytes
    implicit val decoder: CirceCodec[TestData] = deriveCodec
    val decoded = implicitly[Decoder[TestData]].decodeSafe(data)
    import org.scalatest.EitherValues._
    decoded.left.value mustBe an[DecodingFailure]
  }

}
