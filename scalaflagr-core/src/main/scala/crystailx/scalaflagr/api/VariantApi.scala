package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestHandler
import crystailx.scalaflagr.data.{ CreateVariantRequest, UpdateVariantRequest, Variant }

import scala.language.implicitConversions

trait VariantApi {
  private val apiBasePath: String = "/flags/%d/variants"
  import RequestHandler._

  def createVariant(
    flagID: Long,
    body: CreateVariantRequest
  ): RequestHandler[CreateVariantRequest, Variant] =
    post(apiBasePath format flagID, body)

  def deleteVariant(flagID: Long, variantID: Long): RequestHandler[Nothing, Unit] =
    delete(s"$apiBasePath/$variantID" format flagID)

  def flagVariants(flagID: Long): RequestHandler[Nothing, List[Variant]] =
    get(apiBasePath format flagID)

  def updateVariant(
    flagID: Long,
    variantID: Long,
    body: UpdateVariantRequest
  ): RequestHandler[UpdateVariantRequest, Variant] =
    put(s"$apiBasePath/$variantID" format flagID, body)
}
