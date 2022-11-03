package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestHandler
import crystailx.scalaflagr.data.{ CreateTagRequest, FindTagsParam, Tag }

import scala.language.implicitConversions

trait TagApi {

  private val apiBasePath: String = "/flags/%d/tags"
  private val globalTagApiBasePath: String = "/tags"
  import RequestHandler._

  def createTag(flagID: Long, body: CreateTagRequest): RequestHandler[CreateTagRequest, Tag] =
    post(apiBasePath format flagID, body)

  def deleteTag(flagID: Long, tagID: Long): RequestHandler[Nothing, Unit] =
    delete(s"$apiBasePath/$tagID" format flagID)

  def findTags(param: FindTagsParam): RequestHandler[Nothing, List[Tag]] =
    get(globalTagApiBasePath, buildParamsMap(param))

  def flagTags(flagID: Long): RequestHandler[Nothing, List[Tag]] =
    get(apiBasePath format flagID)
}
