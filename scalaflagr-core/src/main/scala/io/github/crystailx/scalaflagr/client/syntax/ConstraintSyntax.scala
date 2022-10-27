package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.{ CreateConstraintRequest, UpdateConstraintRequest }

trait ConstraintSyntax {

  def createConstraintRequest(): CreateConstraintRequest = CreateConstraintRequest("", "", "")
  def updateConstraintRequest(): UpdateConstraintRequest = UpdateConstraintRequest("", "", "")

}
