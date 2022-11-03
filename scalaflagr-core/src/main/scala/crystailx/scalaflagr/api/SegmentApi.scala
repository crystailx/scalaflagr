package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestHandler
import crystailx.scalaflagr.data.{
  CreateSegmentRequest,
  ReorderSegmentRequest,
  Segment,
  UpdateSegmentRequest
}

trait SegmentApi {
  private val apiBasePath: String = "/flags/%d/segments"
  import RequestHandler._

  def createSegment(
    flagID: Long,
    body: CreateSegmentRequest
  ): RequestHandler[CreateSegmentRequest, Segment] =
    post(apiBasePath format flagID, body)

  def deleteSegment(flagID: Long, segmentID: Long): RequestHandler[Nothing, Unit] =
    delete(s"$apiBasePath/$segmentID" format flagID)

  def findSegments(flagID: Long): RequestHandler[Nothing, List[Segment]] =
    get(apiBasePath format flagID)

  def updateSegment(
    flagID: Long,
    segmentID: Long,
    body: UpdateSegmentRequest
  ): RequestHandler[UpdateSegmentRequest, Segment] =
    put(s"$apiBasePath/$segmentID" format flagID, body)

  def reorderSegments(
    flagID: Long,
    body: ReorderSegmentRequest
  ): RequestHandler[ReorderSegmentRequest, Unit] =
    put(s"$apiBasePath/reorder" format flagID, body)

}
