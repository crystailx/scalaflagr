package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.{
  CreateSegmentRequest,
  ReorderSegmentRequest,
  UpdateSegmentRequest
}

trait SegmentApi {
  private val apiBasePath: String = "/flags/%d/segments"
  import RequestBuilder._

  def createSegment(
    flagID: Long,
    body: CreateSegmentRequest
  ): RequestBuilder[CreateSegmentRequest] =
    post(apiBasePath format flagID, body)

  def deleteSegment(flagID: Long, segmentID: Long): RequestBuilder[Nothing] =
    delete(s"$apiBasePath/$segmentID" format flagID)

  def findSegments(flagID: Long): RequestBuilder[Nothing] =
    get(apiBasePath format flagID)

  def updateSegment(
    flagID: Long,
    segmentID: Long,
    body: UpdateSegmentRequest
  ): RequestBuilder[UpdateSegmentRequest] =
    put[UpdateSegmentRequest](s"$apiBasePath/$segmentID" format flagID, body)

  def reorderSegments(
    flagID: Long,
    body: ReorderSegmentRequest
  ): RequestBuilder[ReorderSegmentRequest] =
    put(s"$apiBasePath/reorder" format flagID, body)

}
