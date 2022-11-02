package crystailx.scalaflagr.api.syntax

import crystailx.scalaflagr.data.{CreateTagRequest, FindTagsParam}

trait TagSyntax {

  def createTagRequest: CreateTagRequest = CreateTagRequest("")

  def findTagsParam: FindTagsParam = FindTagsParam()
}
