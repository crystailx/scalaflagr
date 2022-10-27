package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.{CreateSegmentRequest, EvalContext, EvaluationBatchRequest, UpdateSegmentRequest}

trait SegmentSyntax {

  def createSegmentRequest(): CreateSegmentRequest = CreateSegmentRequest("", 0)

  def updateSegmentRequest(): UpdateSegmentRequest = UpdateSegmentRequest("", 0)

}
