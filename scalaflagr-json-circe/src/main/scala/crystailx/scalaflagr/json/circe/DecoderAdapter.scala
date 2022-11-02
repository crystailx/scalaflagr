package crystailx.scalaflagr.json.circe

import crystailx.scalaflagr.data._
import crystailx.scalaflagr.json.{ BuiltInDecoders, Decoder }
import io.circe.generic.semiauto._
import io.circe.{ DecodingFailure, HCursor, parser, Decoder => CirceDecoder }

import java.time.Instant

trait DecoderAdapter extends BuiltInDecoders[CirceDecoder] {

  implicit def decoder[T: CirceDecoder]: Decoder[T] =
    new Decoder[T] {

      override def decode(value: RawValue): T =
        parser.decode[T](new String(value)).fold(throw _, identity)

      override def decodeSafe(value: RawValue): Either[Throwable, T] =
        parser.decode[T](new String(value))
    }

  // some default decoders to avoid boilerplate
  implicit val byteArrayDecoder: CirceDecoder[RawValue] = (c: HCursor) =>
    c.focus.map(_.noSpaces.getBytes).toRight(DecodingFailure("failed to decode data to bytes", Nil))
  implicit val constraintDecoder: CirceDecoder[Constraint] = deriveDecoder
  implicit val createConstraintRequestDecoder: CirceDecoder[CreateConstraintRequest] = deriveDecoder
  implicit val createFlagRequestDecoder: CirceDecoder[CreateFlagRequest] = deriveDecoder
  implicit val createSegmentRequestDecoder: CirceDecoder[CreateSegmentRequest] = deriveDecoder
  implicit val createTagRequestDecoder: CirceDecoder[CreateTagRequest] = deriveDecoder
  implicit val createVariantRequestDecoder: CirceDecoder[CreateVariantRequest] = deriveDecoder
  implicit val distributionDecoder: CirceDecoder[Distribution] = deriveDecoder
  implicit val enableFlagRequestDecoder: CirceDecoder[EnableFlagRequest] = deriveDecoder
  implicit val errorDecoder: CirceDecoder[Error] = deriveDecoder
  implicit val evalContextDecoder: CirceDecoder[EvalContext] = deriveDecoder
  implicit val evalDebugLogDecoder: CirceDecoder[EvalDebugLog] = deriveDecoder
  implicit val evalResultDecoder: CirceDecoder[EvalResult] = deriveDecoder
  implicit val evaluationBatchRequestDecoder: CirceDecoder[EvaluationBatchRequest] = deriveDecoder
  implicit val evaluationBatchResponseDecoder: CirceDecoder[EvaluationBatchResponse] = deriveDecoder
  implicit val evaluationEntityDecoder: CirceDecoder[EvaluationEntity] = deriveDecoder
  implicit val findFlagsParamDecoder: CirceDecoder[FindFlagsParam] = deriveDecoder
  implicit val findTagsParamDecoder: CirceDecoder[FindTagsParam] = deriveDecoder
  implicit val flagDecoder: CirceDecoder[Flag] = deriveDecoder
  implicit val flagSnapshotDecoder: CirceDecoder[FlagSnapshot] = deriveDecoder
  implicit val healthDecoder: CirceDecoder[Health] = deriveDecoder
  implicit val instantDecoder: CirceDecoder[Instant] = CirceDecoder.decodeInstant
  implicit val reorderSegmentRequestDecoder: CirceDecoder[ReorderSegmentRequest] = deriveDecoder
  implicit val segmentDebugLogDecoder: CirceDecoder[SegmentDebugLog] = deriveDecoder
  implicit val segmentDecoder: CirceDecoder[Segment] = deriveDecoder
  implicit val tagDecoder: CirceDecoder[Tag] = deriveDecoder
  implicit val updateConstraintRequestDecoder: CirceDecoder[UpdateConstraintRequest] = deriveDecoder

  implicit val updateDistributionRequestDecoder: CirceDecoder[UpdateDistributionsRequest] =
    deriveDecoder
  implicit val updateFlagRequestDecoder: CirceDecoder[UpdateFlagRequest] = deriveDecoder
  implicit val updateSegmentRequestDecoder: CirceDecoder[UpdateSegmentRequest] = deriveDecoder
  implicit val updateVariantRequestDecoder: CirceDecoder[UpdateVariantRequest] = deriveDecoder
  implicit val variantDecoder: CirceDecoder[Variant] = deriveDecoder
}
