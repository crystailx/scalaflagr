package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.UpdateDistributionsRequest

import scala.language.implicitConversions

trait DistributionApi {

  private val apiBasePath: String = "/flags/%d/segments/%d/distributions"

  import RequestBuilder._

  def findDistributions(flagID: Long, segmentID: Long): RequestBuilder[Nothing] =
    get(apiBasePath format (flagID, segmentID))

  def updateDistributions(
    flagID: Long,
    segmentID: Long,
    body: UpdateDistributionsRequest
  ): RequestBuilder[UpdateDistributionsRequest] =
    put(apiBasePath format (flagID, segmentID), body)
}
