package io.github.crystailx.scalaflagr.data

case class UpdateDistributionsRequest(
  distributions: List[Distribution]
) {

  def distributions(value: List[Distribution]): UpdateDistributionsRequest =
    copy(distributions = value)

  def addDistribution(value: Distribution): UpdateDistributionsRequest =
    copy(distributions = distributions :+ value)
}
