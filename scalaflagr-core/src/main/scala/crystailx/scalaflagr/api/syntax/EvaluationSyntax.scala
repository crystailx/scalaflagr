package crystailx.scalaflagr.api.syntax

import crystailx.scalaflagr.data.{ EvalContext, EvaluationBatchRequest }

trait EvaluationSyntax {

  def evalContext: EvalContext = EvalContext()

  def evaluationBatchRequest: EvaluationBatchRequest = EvaluationBatchRequest(List.empty)

}
