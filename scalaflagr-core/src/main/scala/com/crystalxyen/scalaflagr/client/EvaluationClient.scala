package com.crystalxyen.scalaflagr.client

import com.crystalxyen.scalaflagr.data.{
  EvalContext,
  EvalResult,
  EvaluationBatchRequest,
  EvaluationBatchResponse
}

import scala.language.higherKinds

trait EvaluationClient[F[_]] {
  def evaluate(context: EvalContext): F[EvalResult]
  def batchEvaluate(context: EvaluationBatchRequest): F[EvaluationBatchResponse]
}
