package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestHandler
import crystailx.scalaflagr.data._

import scala.language.implicitConversions

trait FlagApi {

  private val apiBasePath: String = "/flags"
  import RequestHandler._

  def createFlag(flagRequest: CreateFlagRequest): RequestHandler[CreateFlagRequest, Flag] =
    post(apiBasePath, flagRequest)

  def deleteFlag(flagID: Long): RequestHandler[Nothing, Unit] =
    delete(s"$apiBasePath/$flagID")

  def findFlags(param: FindFlagsParam): RequestHandler[Nothing, List[Flag]] =
    get(apiBasePath, buildParamsMap(param))

  def flag(flagID: Long): RequestHandler[Nothing, Flag] =
    get(s"$apiBasePath/$flagID")

  def flagEntityTypes: RequestHandler[Nothing, List[String]] =
    get(s"$apiBasePath/entity_types")

  def flagSnapshots(flagID: Long): RequestHandler[Nothing, List[FlagSnapshot]] =
    get(s"$apiBasePath/$flagID/snapshots")

  def updateFlag(
    flagID: Long,
    flagRequest: UpdateFlagRequest
  ): RequestHandler[UpdateFlagRequest, Flag] =
    put(s"$apiBasePath/$flagID", flagRequest)

  def restoreFlag(flagID: Long): RequestHandler[Nothing, Flag] =
    put(s"$apiBasePath/$flagID/restore")

  def enableFlag(
    flagID: Long,
    flagRequest: EnableFlagRequest
  ): RequestHandler[EnableFlagRequest, Flag] =
    put(s"$apiBasePath/$flagID/enabled", flagRequest)
}
