package crystailx.scalaflagr.json

import crystailx.scalaflagr.data._

import java.time.Instant

trait BuiltInEncoders[EncoderType[_]] {
  // some default encoders to avoid boilerplate
  implicit val byteArrayEncoder: EncoderType[RawValue]
  implicit val constraintEncoder: EncoderType[Constraint]
  implicit val createConstraintRequestEncoder: EncoderType[CreateConstraintRequest]
  implicit val createFlagRequestEncoder: EncoderType[CreateFlagRequest]
  implicit val createSegmentRequestEncoder: EncoderType[CreateSegmentRequest]
  implicit val createTagRequestEncoder: EncoderType[CreateTagRequest]
  implicit val createVariantRequestEncoder: EncoderType[CreateVariantRequest]
  implicit val distributionEncoder: EncoderType[Distribution]
  implicit val enableFlagRequestEncoder: EncoderType[EnableFlagRequest]
  implicit val errorEncoder: EncoderType[Error]
  implicit val evalContextEncoder: EncoderType[EvalContext]
  implicit val evalDebugLogEncoder: EncoderType[EvalDebugLog]
  implicit val evalResultEncoder: EncoderType[EvalResult]
  implicit val evaluationBatchRequestEncoder: EncoderType[EvaluationBatchRequest]
  implicit val evaluationBatchResponseEncoder: EncoderType[EvaluationBatchResponse]
  implicit val evaluationEntityEncoder: EncoderType[EvaluationEntity]
  implicit val findFlagsParamEncoder: EncoderType[FindFlagsParam]
  implicit val findTagsParamEncoder: EncoderType[FindTagsParam]
  implicit val flagEncoder: EncoderType[Flag]
  implicit val flagSnapshotEncoder: EncoderType[FlagSnapshot]
  implicit val healthEncoder: EncoderType[Health]
  implicit val instantEncoder: EncoderType[Instant]
  implicit val reorderSegmentRequestEncoder: EncoderType[ReorderSegmentRequest]
  implicit val segmentDebugLogEncoder: EncoderType[SegmentDebugLog]
  implicit val segmentEncoder: EncoderType[Segment]
  implicit val tagEncoder: EncoderType[Tag]
  implicit val updateConstraintRequestEncoder: EncoderType[UpdateConstraintRequest]
  implicit val updateDistributionRequestEncoder: EncoderType[UpdateDistributionsRequest]
  implicit val updateFlagRequestEncoder: EncoderType[UpdateFlagRequest]
  implicit val updateSegmentRequestEncoder: EncoderType[UpdateSegmentRequest]
  implicit val updateVariantRequestEncoder: EncoderType[UpdateVariantRequest]
  implicit val variantEncoder: EncoderType[Variant]
}
