package crystailx.scalaflagr.api.syntax

import crystailx.scalaflagr.data.UpdateDistributionsRequest

trait DistributionSyntax {

  def updateDistributionsRequest: UpdateDistributionsRequest =
    UpdateDistributionsRequest(List.empty)

}
