package com.crystailx.scalaflagr.client

import com.crystailx.scalaflagr.data.{
  EvalContext,
  EvalResult,
  EvaluationBatchRequest,
  EvaluationBatchResponse
}
import com.crystailx.scalaflagr.effect.Functor
import com.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.higherKinds

trait EvaluationClient[F[_]] extends HttpClient[F] {
  val config: FlagrConfig
  import config._

  def evaluate(
    context: EvalContext
  )(
    implicit
    functor: Functor[F],
    encoder: Encoder[EvalContext],
    decoder: Decoder[EvalResult]
  ): F[EvalResult] =
    functor.map(send(s"$host$basePath/evaluation", encoder.encode(context)))(
      s => decoder.decode(s).copy(rawValue = Some(s))
    )

  def batchEvaluate(
    context: EvaluationBatchRequest
  )(
    implicit
    functor: Functor[F],
    encoder: Encoder[EvaluationBatchRequest],
    decoder: Decoder[EvaluationBatchResponse]
  ): F[EvaluationBatchResponse] =
    functor.map(send(s"$host$basePath/evaluation/batch", encoder.encode(context)))(decoder.decode)

}
