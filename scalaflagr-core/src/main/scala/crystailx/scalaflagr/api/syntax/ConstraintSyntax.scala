package crystailx.scalaflagr.api.syntax

import crystailx.scalaflagr.data.{ CreateConstraintRequest, UpdateConstraintRequest }

trait ConstraintSyntax {

  def createConstraintRequest(): CreateConstraintRequest = CreateConstraintRequest("", "", "")
  def updateConstraintRequest(): UpdateConstraintRequest = UpdateConstraintRequest("", "", "")

}
