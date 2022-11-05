package crystailx.scalaflagr.json

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.reflect.ClassTag

abstract class AdapterBase[DecodeFailure: ClassTag] extends AnyFlatSpec with Matchers {

  val encoder: Encoder[TestData]
  val decoder: Decoder[TestData]

  it must "encode using adapted json encoders" in {
    val data = TestData("Tester", Some(30))
    val encoded = encoder.encode(data)
    new String(encoded) mustBe """{"name":"Tester","age":30}"""
  }

  it must "decode using adapted json decoders" in {
    val data = """{"name":"Flagr"}""".getBytes
    val decoded = decoder.decode(data)
    decoded mustBe TestData("Flagr", None)
  }

  it must "decode safely using adapted json decoders" in {
    val data = """{"invalid":"data"}""".getBytes
    val decoded = decoder.decodeSafe(data)
    import org.scalatest.EitherValues._
    decoded.left.value mustBe an[DecodeFailure]
  }

}
