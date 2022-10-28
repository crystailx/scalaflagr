package io.github.crystailx.scalaflagr.json.play

import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.json.{ BuiltInEncoders, Encoder }
import play.api.libs.json.{ Json, Writes => PlayEncoder }

import java.time.Instant

trait EncoderAdapter extends BuiltInEncoders[PlayEncoder] {

  implicit def encoder[T](implicit encoder: PlayEncoder[T]): Encoder[T] = (body: T) =>
    encoder.writes(body).toString().getBytes

  // some default encoders to avoid boilerplate
  implicit val byteArrayEncoder: PlayEncoder[RawValue] = (o: RawValue) => Json.parse(new String(o))
  implicit val constraintEncoder: PlayEncoder[Constraint] = Json.writes
  implicit val createConstraintRequestEncoder: PlayEncoder[CreateConstraintRequest] = Json.writes
  implicit val createFlagRequestEncoder: PlayEncoder[CreateFlagRequest] = Json.writes
  implicit val createSegmentRequestEncoder: PlayEncoder[CreateSegmentRequest] = Json.writes
  implicit val createTagRequestEncoder: PlayEncoder[CreateTagRequest] = Json.writes
  implicit val createVariantRequestEncoder: PlayEncoder[CreateVariantRequest] = Json.writes
  implicit val distributionEncoder: PlayEncoder[Distribution] = Json.writes
  implicit val enableFlagRequestEncoder: PlayEncoder[EnableFlagRequest] = Json.writes
  implicit val errorEncoder: PlayEncoder[Error] = Json.writes
  implicit val evalContextEncoder: PlayEncoder[EvalContext] = Json.writes
  implicit val evalDebugLogEncoder: PlayEncoder[EvalDebugLog] = Json.writes
  implicit val evalResultEncoder: PlayEncoder[EvalResult] = Json.writes
  implicit val evaluationBatchRequestEncoder: PlayEncoder[EvaluationBatchRequest] = Json.writes
  implicit val evaluationBatchResponseEncoder: PlayEncoder[EvaluationBatchResponse] = Json.writes
  implicit val evaluationEntityEncoder: PlayEncoder[EvaluationEntity] = Json.writes
  implicit val findFlagsParamEncoder: PlayEncoder[FindFlagsParam] = Json.writes
  implicit val findTagsParamEncoder: PlayEncoder[FindTagsParam] = Json.writes
  implicit val flagSnapshotEncoder: PlayEncoder[FlagSnapshot] = Json.writes
  implicit val healthEncoder: PlayEncoder[Health] = Json.writes
  implicit val instantEncoder: PlayEncoder[Instant] = PlayEncoder.DefaultInstantWrites
  implicit val reorderSegmentRequestEncoder: PlayEncoder[ReorderSegmentRequest] = Json.writes
  implicit val segmentDebugLogEncoder: PlayEncoder[SegmentDebugLog] = Json.writes
  implicit val segmentEncoder: PlayEncoder[Segment] = Json.writes
  implicit val tagEncoder: PlayEncoder[Tag] = Json.writes
  implicit val updateConstraintRequestEncoder: PlayEncoder[UpdateConstraintRequest] = Json.writes

  implicit val updateDistributionRequestEncoder: PlayEncoder[UpdateDistributionsRequest] =
    Json.writes
  implicit val updateFlagRequestEncoder: PlayEncoder[UpdateFlagRequest] = Json.writes
  implicit val updateSegmentRequestEncoder: PlayEncoder[UpdateSegmentRequest] = Json.writes
  implicit val updateVariantRequestEncoder: PlayEncoder[UpdateVariantRequest] = Json.writes
  implicit val variantEncoder: PlayEncoder[Variant] = Json.writes
  // This must be the last encoder
  implicit val flagEncoder: PlayEncoder[Flag] = Json.writes

}
