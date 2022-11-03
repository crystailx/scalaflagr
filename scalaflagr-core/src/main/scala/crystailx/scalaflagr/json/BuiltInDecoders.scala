package crystailx.scalaflagr.json

import crystailx.scalaflagr.data._

import java.time.Instant

trait BuiltInDecoders[DecoderType[_]] {
  // some default decoders to avoid boilerplate
  implicit val byteArrayDecoder: DecoderType[RawValue]
  implicit val constraintDecoder: DecoderType[Constraint]
  implicit val createConstraintRequestDecoder: DecoderType[CreateConstraintRequest]
  implicit val createFlagRequestDecoder: DecoderType[CreateFlagRequest]
  implicit val createSegmentRequestDecoder: DecoderType[CreateSegmentRequest]
  implicit val createTagRequestDecoder: DecoderType[CreateTagRequest]
  implicit val createVariantRequestDecoder: DecoderType[CreateVariantRequest]
  implicit val distributionDecoder: DecoderType[Distribution]
  implicit val enableFlagRequestDecoder: DecoderType[EnableFlagRequest]
  implicit val errorDecoder: DecoderType[Error]
  implicit val evalContextDecoder: DecoderType[EvalContext]
  implicit val evalDebugLogDecoder: DecoderType[EvalDebugLog]
  implicit val evalResultDecoder: DecoderType[EvalResult]
  implicit val evaluationBatchRequestDecoder: DecoderType[EvaluationBatchRequest]
  implicit val evaluationBatchResponseDecoder: DecoderType[EvaluationBatchResponse]
  implicit val evaluationEntityDecoder: DecoderType[EvaluationEntity]
  implicit val findFlagsParamDecoder: DecoderType[FindFlagsParam]
  implicit val findTagsParamDecoder: DecoderType[FindTagsParam]
  implicit val flagDecoder: DecoderType[Flag]
  implicit val flagSnapshotDecoder: DecoderType[FlagSnapshot]
  implicit val healthDecoder: DecoderType[Health]
  implicit val instantDecoder: DecoderType[Instant]
  implicit val reorderSegmentRequestDecoder: DecoderType[ReorderSegmentRequest]
  implicit val segmentDebugLogDecoder: DecoderType[SegmentDebugLog]
  implicit val segmentDecoder: DecoderType[Segment]
  implicit val tagDecoder: DecoderType[Tag]
  implicit val updateConstraintRequestDecoder: DecoderType[UpdateConstraintRequest]
  implicit val updateDistributionRequestDecoder: DecoderType[UpdateDistributionsRequest]
  implicit val updateFlagRequestDecoder: DecoderType[UpdateFlagRequest]
  implicit val updateSegmentRequestDecoder: DecoderType[UpdateSegmentRequest]
  implicit val updateVariantRequestDecoder: DecoderType[UpdateVariantRequest]
  implicit val variantDecoder: DecoderType[Variant]
}
