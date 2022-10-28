package io.github.crystailx.scalaflagr.json.play

import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.json.{ BuiltInDecoders, Decoder }
import play.api.libs.json.{ JsResult, JsResultException, JsValue, Json, Reads => PlayDecoder }

import java.time.Instant
import scala.util.Try

trait DecoderAdapter extends BuiltInDecoders[PlayDecoder] {

  implicit def decoder[T: PlayDecoder]: Decoder[T] =
    new Decoder[T] {
      override def decode(value: RawValue): T = Json.parse(value).as[T]

      override def decodeSafe(value: RawValue): Either[Throwable, T] =
        Json.parse(value).validate[T].asEither.left.map(JsResultException)
    }

  // some default decoders to avoid boilerplate
  implicit val byteArrayDecoder: PlayDecoder[RawValue] = (json: JsValue) =>
    JsResult.fromTry(Try(json.toString().getBytes))
  implicit val constraintDecoder: PlayDecoder[Constraint] = Json.reads
  implicit val createConstraintRequestDecoder: PlayDecoder[CreateConstraintRequest] = Json.reads
  implicit val createFlagRequestDecoder: PlayDecoder[CreateFlagRequest] = Json.reads
  implicit val createSegmentRequestDecoder: PlayDecoder[CreateSegmentRequest] = Json.reads
  implicit val createTagRequestDecoder: PlayDecoder[CreateTagRequest] = Json.reads
  implicit val createVariantRequestDecoder: PlayDecoder[CreateVariantRequest] = Json.reads
  implicit val distributionDecoder: PlayDecoder[Distribution] = Json.reads
  implicit val enableFlagRequestDecoder: PlayDecoder[EnableFlagRequest] = Json.reads
  implicit val errorDecoder: PlayDecoder[Error] = Json.reads
  implicit val evalContextDecoder: PlayDecoder[EvalContext] = Json.reads
  implicit val evalDebugLogDecoder: PlayDecoder[EvalDebugLog] = Json.reads
  implicit val evalResultDecoder: PlayDecoder[EvalResult] = Json.reads
  implicit val evaluationBatchRequestDecoder: PlayDecoder[EvaluationBatchRequest] = Json.reads
  implicit val evaluationBatchResponseDecoder: PlayDecoder[EvaluationBatchResponse] = Json.reads
  implicit val evaluationEntityDecoder: PlayDecoder[EvaluationEntity] = Json.reads
  implicit val findFlagsParamDecoder: PlayDecoder[FindFlagsParam] = Json.reads
  implicit val findTagsParamDecoder: PlayDecoder[FindTagsParam] = Json.reads
  implicit val flagSnapshotDecoder: PlayDecoder[FlagSnapshot] = Json.reads
  implicit val healthDecoder: PlayDecoder[Health] = Json.reads
  implicit val reorderSegmentRequestDecoder: PlayDecoder[ReorderSegmentRequest] = Json.reads
  implicit val segmentDebugLogDecoder: PlayDecoder[SegmentDebugLog] = Json.reads
  implicit val segmentDecoder: PlayDecoder[Segment] = Json.reads
  implicit val tagDecoder: PlayDecoder[Tag] = Json.reads
  implicit val updateConstraintRequestDecoder: PlayDecoder[UpdateConstraintRequest] = Json.reads

  implicit val updateDistributionRequestDecoder: PlayDecoder[UpdateDistributionsRequest] =
    Json.reads
  implicit val updateFlagRequestDecoder: PlayDecoder[UpdateFlagRequest] = Json.reads
  implicit val updateSegmentRequestDecoder: PlayDecoder[UpdateSegmentRequest] = Json.reads
  implicit val updateVariantRequestDecoder: PlayDecoder[UpdateVariantRequest] = Json.reads
  implicit val variantDecoder: PlayDecoder[Variant] = Json.reads
  implicit val instantDecoder: PlayDecoder[Instant] = PlayDecoder.DefaultInstantReads
  // This must be the last decoder
  implicit val flagDecoder: PlayDecoder[Flag] = Json.reads

}
