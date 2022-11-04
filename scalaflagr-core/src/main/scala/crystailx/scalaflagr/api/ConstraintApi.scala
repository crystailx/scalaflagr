package crystailx.scalaflagr.api

import crystailx.scalaflagr.{ BodyRequestHandler, NoBodyRequestHandler, RequestHandler }
import crystailx.scalaflagr.data.{ Constraint, CreateConstraintRequest, UpdateConstraintRequest }

import scala.language.implicitConversions

trait ConstraintApi {

  private val apiBasePath: String = "/flags/%d/segments/%d/constraints"
  import RequestHandler._

  def createConstraint(
    flagID: Long,
    segmentID: Long,
    body: CreateConstraintRequest
  ): BodyRequestHandler[CreateConstraintRequest, Constraint] =
    post(apiBasePath format (flagID, segmentID), body)

  def deleteConstraint(
    flagID: Long,
    segmentID: Long,
    constraintID: Long
  ): NoBodyRequestHandler[Unit] =
    delete(s"$apiBasePath/$constraintID" format (flagID, segmentID))

  def findConstraints(flagID: Long, segmentID: Long): NoBodyRequestHandler[List[Constraint]] =
    get(apiBasePath format (flagID, segmentID))

  def updateConstraint(
    flagID: Long,
    segmentID: Long,
    constraintID: Long,
    body: UpdateConstraintRequest
  ): BodyRequestHandler[UpdateConstraintRequest, Constraint] =
    put(s"$apiBasePath/$constraintID" format (flagID, segmentID), body)
}
