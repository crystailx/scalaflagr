package crystailx.scalaflagr.api

import crystailx.scalaflagr.{ BodyRequestHandler, NoBodyRequestHandler, RequestHandler }
import crystailx.scalaflagr.data.{ CreateTagRequest, FindTagsParam, Tag }

import scala.language.implicitConversions

trait TagApi {

  private val apiBasePath: String = "/flags/%d/tags"
  private val globalTagApiBasePath: String = "/tags"
  import RequestHandler._

  def createTag(flagID: Long, body: CreateTagRequest): BodyRequestHandler[CreateTagRequest, Tag] =
    post(apiBasePath format flagID, body)

  def deleteTag(flagID: Long, tagID: Long): NoBodyRequestHandler[Unit] =
    delete(s"$apiBasePath/$tagID" format flagID)

  def findTags(param: FindTagsParam): NoBodyRequestHandler[List[Tag]] =
    get(globalTagApiBasePath, buildParamsMap(param))

  def flagTags(flagID: Long): NoBodyRequestHandler[List[Tag]] =
    get(apiBasePath format flagID)
}
