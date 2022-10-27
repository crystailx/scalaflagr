package io.github.crystailx.scalaflagr.client

import io.github.crystailx.scalaflagr.client.api.{
  ConstraintApi,
  DistributionApi,
  FlagApi,
  HealthApi,
  SegmentApi,
  TagApi,
  VariantApi
}
import io.github.crystailx.scalaflagr.client.syntax._

trait ManagerClient[F[_]]
    extends FlagApi[F]
    with HealthApi[F]
    with TagApi[F]
    with SegmentApi[F]
    with DistributionApi[F]
    with ConstraintApi[F]
    with VariantApi[F]
    with FlagSyntax
    with TagSyntax
    with SegmentSyntax
    with DistributionSyntax
    with ConstraintSyntax
    with VariantSyntax
