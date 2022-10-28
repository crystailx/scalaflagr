package io.github.crystailx.scalaflagr.json.circe

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ parser, Encoder => CirceEncoder }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.json.{ BuiltInEncoders, Encoder }

import java.time.Instant

trait EncoderAdapter extends BuiltInEncoders[CirceEncoder] {

  implicit def encoder[T](implicit encoder: CirceEncoder[T]): Encoder[T] =
    (body: T) => encoder.apply(body).noSpaces.getBytes

  // some default encoders to avoid boilerplate
  implicit val byteArrayEncoder: CirceEncoder[RawValue] = (a: RawValue) =>
    parser.parse(new String(a)).fold(throw _, identity)
  implicit val constraintEncoder: CirceEncoder[Constraint] = deriveEncoder
  implicit val createConstraintRequestEncoder: CirceEncoder[CreateConstraintRequest] = deriveEncoder
  implicit val createFlagRequestEncoder: CirceEncoder[CreateFlagRequest] = deriveEncoder
  implicit val createSegmentRequestEncoder: CirceEncoder[CreateSegmentRequest] = deriveEncoder
  implicit val createTagRequestEncoder: CirceEncoder[CreateTagRequest] = deriveEncoder
  implicit val createVariantRequestEncoder: CirceEncoder[CreateVariantRequest] = deriveEncoder
  implicit val distributionEncoder: CirceEncoder[Distribution] = deriveEncoder
  implicit val enableFlagRequestEncoder: CirceEncoder[EnableFlagRequest] = deriveEncoder
  implicit val errorEncoder: CirceEncoder[Error] = deriveEncoder
  implicit val evalContextEncoder: CirceEncoder[EvalContext] = deriveEncoder
  implicit val evalDebugLogEncoder: CirceEncoder[EvalDebugLog] = deriveEncoder
  implicit val evalResultEncoder: CirceEncoder[EvalResult] = deriveEncoder
  implicit val evaluationBatchRequestEncoder: CirceEncoder[EvaluationBatchRequest] = deriveEncoder
  implicit val evaluationBatchResponseEncoder: CirceEncoder[EvaluationBatchResponse] = deriveEncoder
  implicit val evaluationEntityEncoder: CirceEncoder[EvaluationEntity] = deriveEncoder
  implicit val findFlagsParamEncoder: CirceEncoder[FindFlagsParam] = deriveEncoder
  implicit val findTagsParamEncoder: CirceEncoder[FindTagsParam] = deriveEncoder
  implicit val flagEncoder: CirceEncoder[Flag] = deriveEncoder
  implicit val flagSnapshotEncoder: CirceEncoder[FlagSnapshot] = deriveEncoder
  implicit val healthEncoder: CirceEncoder[Health] = deriveEncoder
  implicit val instantEncoder: CirceEncoder[Instant] = CirceEncoder.encodeInstant
  implicit val reorderSegmentRequestEncoder: CirceEncoder[ReorderSegmentRequest] = deriveEncoder
  implicit val segmentDebugLogEncoder: CirceEncoder[SegmentDebugLog] = deriveEncoder
  implicit val segmentEncoder: CirceEncoder[Segment] = deriveEncoder
  implicit val tagEncoder: CirceEncoder[Tag] = deriveEncoder
  implicit val updateConstraintRequestEncoder: CirceEncoder[UpdateConstraintRequest] = deriveEncoder

  implicit val updateDistributionRequestEncoder: CirceEncoder[UpdateDistributionsRequest] =
    deriveEncoder
  implicit val updateFlagRequestEncoder: CirceEncoder[UpdateFlagRequest] = deriveEncoder
  implicit val updateSegmentRequestEncoder: CirceEncoder[UpdateSegmentRequest] = deriveEncoder
  implicit val updateVariantRequestEncoder: CirceEncoder[UpdateVariantRequest] = deriveEncoder
  implicit val variantEncoder: CirceEncoder[Variant] = deriveEncoder
}
