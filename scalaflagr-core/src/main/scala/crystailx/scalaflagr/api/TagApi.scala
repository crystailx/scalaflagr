package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder
import crystailx.scalaflagr.data.{ CreateTagRequest, FindTagsParam }

import scala.language.implicitConversions

trait TagApi {

  private val apiBasePath: String = "/flags/%d/tags"
  private val globalTagApiBasePath: String = "/tags"
  import RequestBuilder._

  def createTag(flagID: Long, body: CreateTagRequest): RequestBuilder[CreateTagRequest] =
    post(apiBasePath format flagID, body)

  def deleteTag(flagID: Long, tagID: Long): RequestBuilder[Nothing] =
    delete(s"$apiBasePath/$tagID" format flagID)

  def findTags(param: FindTagsParam): RequestBuilder[Nothing] =
    get(globalTagApiBasePath, buildParamsMap(param))

  def flagTags(flagID: Long): RequestBuilder[Nothing] =
    get(apiBasePath format flagID)
}
