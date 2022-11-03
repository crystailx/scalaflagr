package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestHandler
import crystailx.scalaflagr.data.{
  EvalContext,
  EvalResult,
  EvaluationBatchRequest,
  EvaluationBatchResponse
}

trait EvaluationApi {
  import RequestHandler._

  def evaluate(
    context: EvalContext
  ): RequestHandler[EvalContext, EvalResult] =
    post("/evaluation", context)

  def batchEvaluate(
    context: EvaluationBatchRequest
  ): RequestHandler[EvaluationBatchRequest, EvaluationBatchResponse] =
    post("/evaluation/batch", context)

}
