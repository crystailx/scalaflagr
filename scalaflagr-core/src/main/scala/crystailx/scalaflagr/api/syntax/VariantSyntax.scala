package crystailx.scalaflagr.api.syntax

import crystailx.scalaflagr.data.{CreateVariantRequest, UpdateVariantRequest}

trait VariantSyntax {

  def createVariantRequest: CreateVariantRequest = CreateVariantRequest("")

  def updateVariantRequest: UpdateVariantRequest = UpdateVariantRequest("")
}
