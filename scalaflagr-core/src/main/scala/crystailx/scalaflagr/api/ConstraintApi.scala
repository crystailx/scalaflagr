package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.{ CreateConstraintRequest, UpdateConstraintRequest }

import scala.language.implicitConversions

trait ConstraintApi {

  private val apiBasePath: String = "/flags/%d/segments/%d/constraints"
  import RequestBuilder._

  def createConstraint(
    flagID: Long,
    segmentID: Long,
    body: CreateConstraintRequest
  ): RequestBuilder[CreateConstraintRequest] =
    post(apiBasePath format (flagID, segmentID), body)

  def deleteConstraint(flagID: Long, segmentID: Long, constraintID: Long): RequestBuilder[Nothing] =
    delete(s"$apiBasePath/$constraintID" format (flagID, segmentID))

  def findConstraints(flagID: Long, segmentID: Long): RequestBuilder[Nothing] =
    get(apiBasePath format (flagID, segmentID))

  def updateConstraint(
    flagID: Long,
    segmentID: Long,
    constraintID: Long,
    body: UpdateConstraintRequest
  ): RequestBuilder[UpdateConstraintRequest] =
    put(s"$apiBasePath/$constraintID" format (flagID, segmentID), body)
}
