package io.github.crystailx.scalaflagr.json.circe

import io.circe.generic.semiauto.deriveCodec
import io.circe.{ Codec => CirceCodec, Encoder => CirceEncoder }
import io.github.crystailx.scalaflagr.data.{ Tag, Variant }
import io.github.crystailx.scalaflagr.json.Encoder
import org.scalatest.EitherValues._
import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class CodecSpec extends AnyFlatSpec with Matchers {

  private val flagJson: String =
    """{
      |	"dataRecordsEnabled": false,
      |	"description": "test",
      |	"enabled": true,
      |	"id": 1,
      |	"key": "flag-key",
      |	"notes": "some notes",
      |	"segments": [
      |		{
      |			"constraints": [
      |				{
      |					"id": 1,
      |					"operator": "GTE",
      |					"property": "age",
      |					"value": "12"
      |				}
      |			],
      |			"description": "all users",
      |			"distributions": [
      |				{
      |					"id": 1,
      |					"percent": 100,
      |					"variantID": 1,
      |					"variantKey": "v1"
      |				}
      |			],
      |			"id": 1,
      |			"rank": 0,
      |			"rolloutPercent": 100
      |		}
      |	],
      |	"tags": [
      |		{
      |			"id": 1,
      |			"value": "new tag"
      |		}
      |	],
      | "createdBy": "ChangYen",
      | "updatedBy": "CrystailX",
      |	"updatedAt": "2022-10-28T06:01:05.432Z",
      |	"variants": [
      |		{
      |			"attachment": {
      |             "nested": {
      |					"vKey": "vValue"
      |				}
      |			},
      |			"id": 1,
      |			"key": "v1"
      |		}
      |	]
      |}""".stripMargin

  case class Attachment(nested: Map[String, String])
  implicit val variantAttachmentCodec: CirceCodec[Attachment] = deriveCodec

  it must "decode Flag with built in decoders" in {
    val decoded = decoder(flagDecoder).decodeSafe(flagJson.getBytes).value
    decoded.id.value mustBe 1
    decoded.key.value mustBe "flag-key"
    decoded.description mustBe "test"
    decoded.enabled mustBe true
    decoded.dataRecordsEnabled mustBe false
    decoded.entityType mustBe empty
    decoded.notes.value mustBe "some notes"
    decoded.createdBy.value mustBe "ChangYen"
    decoded.updatedBy.value mustBe "CrystailX"
    decoded.updatedAt.value.toEpochMilli mustBe 1666936865432L

    decoded.tags.value must contain only Tag(Some(1), "new tag")
    decoded.variants.value must have size 1
    decoded.variants.value.head.id.value mustBe 1L
    decoded.variants.value.head.key mustBe "v1"
    decoded.variants.value.head
      .attachment[Attachment]
      .value
      .nested must (contain key "vKey" and contain value "vValue")

    decoded.segments.value must have size 1
    val segment = decoded.segments.value.head
    segment.id.value mustBe 1
    segment.rank mustBe 0
    segment.rolloutPercent mustBe 100
    segment.description mustBe "all users"
    segment.constraints.value must have size 1
    val constraint = segment.constraints.value.head
    constraint.id.value mustBe 1
    constraint.property mustBe "age"
    constraint.operator mustBe "GTE"
    constraint.value mustBe "12"
    segment.distributions.value must have size 1
    val distribution = segment.distributions.value.head
    distribution.id.value mustBe 1
    distribution.percent mustBe 100
    distribution.variantID mustBe 1
    distribution.variantKey mustBe "v1"
  }

  it must "encode hidden(private/protected) fields" in {
    val variant = Variant(
      key = "vKey",
      attachment = Some(implicitly[Encoder[Attachment]].encode(Attachment(Map("wow" -> "cool"))))
    )
    val encoded = new String(encoder(variantEncoder).encode(variant))
    println(encoded)
    encoded.contains("\"wow\"") mustBe true
    encoded.contains("\"cool\"") mustBe true
  }

}
