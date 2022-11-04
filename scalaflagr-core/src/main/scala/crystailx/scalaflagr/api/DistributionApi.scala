package crystailx.scalaflagr.api

import crystailx.scalaflagr.{ BodyRequestHandler, NoBodyRequestHandler, RequestHandler }
import crystailx.scalaflagr.data.{ Distribution, UpdateDistributionsRequest }

import scala.language.implicitConversions

trait DistributionApi {

  private val apiBasePath: String = "/flags/%d/segments/%d/distributions"

  import RequestHandler._

  def findDistributions(
    flagID: Long,
    segmentID: Long
  ): NoBodyRequestHandler[List[Distribution]] =
    get(apiBasePath format (flagID, segmentID))

  def updateDistributions(
    flagID: Long,
    segmentID: Long,
    body: UpdateDistributionsRequest
  ): BodyRequestHandler[UpdateDistributionsRequest, List[Distribution]] =
    put(apiBasePath format (flagID, segmentID), body)
}
