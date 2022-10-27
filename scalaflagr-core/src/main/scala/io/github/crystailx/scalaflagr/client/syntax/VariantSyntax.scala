package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.{ CreateVariantRequest, UpdateVariantRequest }

trait VariantSyntax {

  def createVariantRequest(): CreateVariantRequest = CreateVariantRequest("")

  def updateVariantRequest(): UpdateVariantRequest = UpdateVariantRequest("")
}
