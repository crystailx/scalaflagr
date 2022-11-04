package crystailx.scalaflagr.api

import crystailx.scalaflagr.{ BodyRequestHandler, RequestHandler }
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
  ): BodyRequestHandler[EvalContext, EvalResult] =
    post("/evaluation", context)

  def batchEvaluate(
    context: EvaluationBatchRequest
  ): BodyRequestHandler[EvaluationBatchRequest, EvaluationBatchResponse] =
    post("/evaluation/batch", context)

}
