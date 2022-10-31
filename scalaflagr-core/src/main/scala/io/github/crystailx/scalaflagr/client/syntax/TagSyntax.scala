package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.{ CreateTagRequest, FindTagsParam }

trait TagSyntax {

  def createTagRequest: CreateTagRequest = CreateTagRequest("")

  def findTagsParam: FindTagsParam = FindTagsParam()
}
