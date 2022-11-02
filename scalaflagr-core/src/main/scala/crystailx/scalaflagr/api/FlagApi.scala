package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.{
  CreateFlagRequest,
  EnableFlagRequest,
  FindFlagsParam,
  UpdateFlagRequest
}

import scala.language.implicitConversions

trait FlagApi {

  private val apiBasePath: String = "/flags"
  import RequestBuilder._

  def createFlag(flagRequest: CreateFlagRequest): RequestBuilder[CreateFlagRequest] =
    post(apiBasePath, flagRequest)

  def deleteFlag(flagID: Long): RequestBuilder[Nothing] =
    delete(s"$apiBasePath/$flagID")

  def findFlags(param: FindFlagsParam): RequestBuilder[Nothing] =
    get(apiBasePath, buildParamsMap(param))

  def flag(flagID: Long): RequestBuilder[Nothing] =
    get(s"$apiBasePath/$flagID")

  def flagEntityTypes: RequestBuilder[Nothing] =
    get(s"$apiBasePath/entity_types")

  def flagSnapshots(flagID: Long): RequestBuilder[Nothing] =
    get(s"$apiBasePath/$flagID/snapshots")

  def updateFlag(flagID: Long, flagRequest: UpdateFlagRequest): RequestBuilder[UpdateFlagRequest] =
    put(s"$apiBasePath/$flagID", flagRequest)

  def restoreFlag(flagID: Long): RequestBuilder[Nothing] =
    put(s"$apiBasePath/$flagID/restore")

  def enableFlag(flagID: Long, flagRequest: EnableFlagRequest): RequestBuilder[EnableFlagRequest] =
    put(s"$apiBasePath/$flagID/enabled", flagRequest)
}
