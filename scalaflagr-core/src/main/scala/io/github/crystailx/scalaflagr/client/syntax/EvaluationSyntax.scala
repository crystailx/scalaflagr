package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.{ EvalContext, EvaluationBatchRequest }

trait EvaluationSyntax {

  def evalContext(): EvalContext = EvalContext()

  def evaluationBatchRequest(): EvaluationBatchRequest = EvaluationBatchRequest(List.empty)

}
