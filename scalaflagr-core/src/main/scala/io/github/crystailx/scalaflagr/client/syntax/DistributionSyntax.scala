package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.UpdateDistributionsRequest

trait DistributionSyntax {

  def updateDistributionsRequest(): UpdateDistributionsRequest =
    UpdateDistributionsRequest(List.empty)

}
