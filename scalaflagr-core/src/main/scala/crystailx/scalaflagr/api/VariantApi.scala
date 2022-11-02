package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.{ CreateVariantRequest, UpdateVariantRequest }

import scala.language.implicitConversions

trait VariantApi {
  private val apiBasePath: String = "/flags/%d/variants"
  import RequestBuilder._

  def createVariant(
    flagID: Long,
    body: CreateVariantRequest
  ): RequestBuilder[CreateVariantRequest] =
    post(apiBasePath format flagID, body)

  def deleteVariant(flagID: Long, variantID: Long): RequestBuilder[Nothing] =
    delete(s"$apiBasePath/$variantID" format flagID)

  def flagVariants(flagID: Long): RequestBuilder[Nothing] =
    get(apiBasePath format flagID)

  def updateVariant(
    flagID: Long,
    variantID: Long,
    body: UpdateVariantRequest
  ): RequestBuilder[UpdateVariantRequest] =
    put(s"$apiBasePath/$variantID" format flagID, body)
}
