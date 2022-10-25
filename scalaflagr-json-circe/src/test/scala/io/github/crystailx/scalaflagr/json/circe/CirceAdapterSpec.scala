package io.github.crystailx.scalaflagr.json.circe

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.flatspec.AnyFlatSpec
import io.circe.{ DecodingFailure, Decoder => CirceDecoder, Encoder => CirceEncoder }
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import org.scalatest.matchers.must.Matchers

class CirceAdapterSpec extends AnyFlatSpec with LazyLogging with Matchers {

  case class TestData(name: String, age: Option[Int])

  it must "encode using circe encoders" in {
    val data = TestData("Tester", Some(30))
    implicit val encoder: CirceEncoder[TestData] = deriveEncoder
    val encoded = implicitly[Encoder[TestData]].encode(data)
    encoded mustBe """{"name":"Tester","age":30}"""
  }

  it must "decode using circe decoders" in {
    val data = """{"name":"Flagr"}"""
    implicit val decoder: CirceDecoder[TestData] = deriveDecoder
    val decoded = implicitly[Decoder[TestData]].decode(data)
    decoded mustBe TestData("Flagr", None)
  }

  it must "decode safely using circe decoders" in {
    val data = """{"invalid":"data"}"""
    implicit val decoder: CirceDecoder[TestData] = deriveDecoder
    val decoded = implicitly[Decoder[TestData]].decodeSafe(data)
    import org.scalatest.EitherValues._
    decoded.left.value mustBe an[DecodingFailure]
  }

}
