package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.{ EvalContext, EvaluationBatchRequest }

trait EvaluationApi {
  import RequestBuilder._

  def evaluate(
    context: EvalContext
  ): RequestBuilder[EvalContext] =
    post("/evaluation", context)

  def batchEvaluate(
    context: EvaluationBatchRequest
  ): RequestBuilder[EvaluationBatchRequest] =
    post("/evaluation/batch", context)

}
