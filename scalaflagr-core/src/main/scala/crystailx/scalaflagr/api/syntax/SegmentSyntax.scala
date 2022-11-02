package crystailx.scalaflagr.api.syntax

import crystailx.scalaflagr.data.{ CreateSegmentRequest, UpdateSegmentRequest }

trait SegmentSyntax {

  def createSegmentRequest: CreateSegmentRequest = CreateSegmentRequest("", 0)

  def updateSegmentRequest: UpdateSegmentRequest = UpdateSegmentRequest("", 0)

}
