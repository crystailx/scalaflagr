package crystailx.scalaflagr.json.play

import com.typesafe.scalalogging.LazyLogging
import crystailx.scalaflagr.json.{Decoder, Encoder}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import play.api.libs.json.{ JsResultException, Json, Reads => PlayDecoder, Writes => PlayEncoder }

class PlayJsonAdapterSpec extends AnyFlatSpec with LazyLogging with Matchers {

  case class TestData(name: String, age: Option[Int])

  it must "encode using play-json encoders" in {
    val data = TestData("Tester", Some(30))
    implicit val encoder: PlayEncoder[TestData] = Json.writes
    val encoded = implicitly[Encoder[TestData]].encode(data)
    new String(encoded) mustBe """{"name":"Tester","age":30}"""
  }

  it must "decode using play-json decoders" in {
    val data = """{"name":"Flagr"}"""
    implicit val decoder: PlayDecoder[TestData] = Json.reads
    val decoded = implicitly[Decoder[TestData]].decode(data.getBytes)
    decoded mustBe TestData("Flagr", None)
  }

  it must "decode safely using play-json decoders" in {
    val data = """{"invalid":"data"}"""
    implicit val decoder: PlayDecoder[TestData] = Json.reads
    val decoded = implicitly[Decoder[TestData]].decodeSafe(data.getBytes)
    import org.scalatest.EitherValues._
    decoded.left.value mustBe an[JsResultException]
  }

}
