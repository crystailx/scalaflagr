package crystailx.scalaflagr.api

import crystailx.scalaflagr.{ BodyRequestHandler, NoBodyRequestHandler, RequestHandler }
import crystailx.scalaflagr.data._

import scala.language.implicitConversions

trait FlagApi {

  private val apiBasePath: String = "/flags"
  import RequestHandler._

  def createFlag(flagRequest: CreateFlagRequest): BodyRequestHandler[CreateFlagRequest, Flag] =
    post(apiBasePath, flagRequest)

  def deleteFlag(flagID: Long): NoBodyRequestHandler[Unit] =
    delete(s"$apiBasePath/$flagID")

  def findFlags(param: FindFlagsParam): NoBodyRequestHandler[List[Flag]] =
    get(apiBasePath, buildParamsMap(param))

  def flag(flagID: Long): NoBodyRequestHandler[Flag] =
    get(s"$apiBasePath/$flagID")

  def flagEntityTypes: NoBodyRequestHandler[List[String]] =
    get(s"$apiBasePath/entity_types")

  def flagSnapshots(flagID: Long): NoBodyRequestHandler[List[FlagSnapshot]] =
    get(s"$apiBasePath/$flagID/snapshots")

  def updateFlag(
    flagID: Long,
    flagRequest: UpdateFlagRequest
  ): BodyRequestHandler[UpdateFlagRequest, Flag] =
    put(s"$apiBasePath/$flagID", flagRequest)

  def restoreFlag(flagID: Long): NoBodyRequestHandler[Flag] =
    put(s"$apiBasePath/$flagID/restore")

  def enableFlag(
    flagID: Long,
    flagRequest: EnableFlagRequest
  ): BodyRequestHandler[EnableFlagRequest, Flag] =
    put(s"$apiBasePath/$flagID/enabled", flagRequest)
}
