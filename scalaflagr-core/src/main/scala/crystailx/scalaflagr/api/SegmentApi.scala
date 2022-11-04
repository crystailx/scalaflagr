package crystailx.scalaflagr.api

import crystailx.scalaflagr.{ BodyRequestHandler, NoBodyRequestHandler, RequestHandler }
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
  ): BodyRequestHandler[CreateSegmentRequest, Segment] =
    post(apiBasePath format flagID, body)

  def deleteSegment(flagID: Long, segmentID: Long): NoBodyRequestHandler[Unit] =
    delete(s"$apiBasePath/$segmentID" format flagID)

  def findSegments(flagID: Long): NoBodyRequestHandler[List[Segment]] =
    get(apiBasePath format flagID)

  def updateSegment(
    flagID: Long,
    segmentID: Long,
    body: UpdateSegmentRequest
  ): BodyRequestHandler[UpdateSegmentRequest, Segment] =
    put(s"$apiBasePath/$segmentID" format flagID, body)

  def reorderSegments(
    flagID: Long,
    body: ReorderSegmentRequest
  ): BodyRequestHandler[ReorderSegmentRequest, Unit] =
    put(s"$apiBasePath/reorder" format flagID, body)

}
