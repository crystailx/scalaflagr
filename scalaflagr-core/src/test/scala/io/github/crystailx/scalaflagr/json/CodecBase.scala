package io.github.crystailx.scalaflagr.json

import io.github.crystailx.scalaflagr.data.{ Flag, Tag, Variant }
import org.scalatest.EitherValues._
import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

trait CodecBase extends AnyFlatSpec with Matchers {

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
      |				"nested": {
      |					"vKey": "vValue"
      |				}
      |			},
      |			"id": 1,
      |			"key": "v1"
      |		}
      |	]
      |}""".stripMargin

  protected val flagAdaptedDecoder: Decoder[Flag]
  protected val variantAdaptedDecoder: Encoder[Variant]

  protected implicit val attachmentAdaptedDecoder: Decoder[Attachment]
  protected implicit val attachmentAdaptedEncoder: Encoder[Attachment]
  it must "decode Flag with built in decoders" in {
    val decoded = flagAdaptedDecoder.decodeSafe(flagJson.getBytes).value
    decoded.id mustBe 1
    decoded.key mustBe "flag-key"
    decoded.description mustBe "test"
    decoded.enabled mustBe true
    decoded.dataRecordsEnabled mustBe false
    decoded.entityType mustBe empty
    decoded.notes.value mustBe "some notes"
    decoded.createdBy.value mustBe "ChangYen"
    decoded.updatedBy.value mustBe "CrystailX"
    decoded.updatedAt.toEpochMilli mustBe 1666936865432L

    decoded.tags must contain only Tag(Some(1), "new tag")
    decoded.variants must have size 1
    decoded.variants.head.id mustBe 1L
    decoded.variants.head.key mustBe "v1"
    decoded.variants.head
      .attachment[Attachment]
      .value
      .nested must (contain key "vKey" and contain value "vValue")

    decoded.segments must have size 1
    val segment = decoded.segments.head
    segment.id mustBe 1
    segment.rank mustBe 0
    segment.rolloutPercent mustBe 100
    segment.description mustBe "all users"
    segment.constraints must have size 1
    val constraint = segment.constraints.head
    constraint.id.value mustBe 1
    constraint.property mustBe "age"
    constraint.operator mustBe "GTE"
    constraint.value mustBe "12"
    segment.distributions must have size 1
    val distribution = segment.distributions.head
    distribution.id.value mustBe 1
    distribution.percent mustBe 100
    distribution.variantID mustBe 1
    distribution.variantKey mustBe "v1"
  }

  it must "encode hidden(private/protected) fields" in {
    val variant = Variant(
      1,
      key = "vKey",
      attachment = Some(attachmentAdaptedEncoder.encode(Attachment(Map("wow" -> "cool"))))
    )
    val encoded = new String(variantAdaptedDecoder.encode(variant))
    println(encoded)
    encoded.contains("\"wow\"") mustBe true
    encoded.contains("\"cool\"") mustBe true
  }
}
