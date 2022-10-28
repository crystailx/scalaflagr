package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data.{
  EvalContext,
  EvalResult,
  EvaluationBatchRequest,
  EvaluationBatchResponse
}
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

trait EvaluationApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  val config: FlagrConfig
  import config._

  def evaluate(
    context: EvalContext
  )(implicit
    encoder: Encoder[EvalContext],
    decoder: Decoder[EvalResult]
  ): F[EvalResult] =
    post(s"$host$basePath/evaluation", context)

  def batchEvaluate(
    context: EvaluationBatchRequest
  )(implicit
    encoder: Encoder[EvaluationBatchRequest],
    decoder: Decoder[EvaluationBatchResponse]
  ): F[EvaluationBatchResponse] =
    post(s"$host$basePath/evaluation/batch", context)

}
