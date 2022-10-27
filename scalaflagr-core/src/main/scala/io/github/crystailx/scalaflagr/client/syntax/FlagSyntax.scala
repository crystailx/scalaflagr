package io.github.crystailx.scalaflagr.client.syntax

import io.github.crystailx.scalaflagr.data.{
  CreateFlagRequest,
  EnableFlagRequest,
  FindFlagsParam,
  UpdateFlagRequest
}

trait FlagSyntax {

  def createFlagRequest(): CreateFlagRequest = CreateFlagRequest("")

  def updateFlagRequest(): UpdateFlagRequest = UpdateFlagRequest()

  def findFlagsParam(): FindFlagsParam = FindFlagsParam()

  def enable: EnableFlagRequest = FlagSyntax.enable

  def disable: EnableFlagRequest = FlagSyntax.disable
}

object FlagSyntax {
  private val enable = EnableFlagRequest(true)
  private val disable = EnableFlagRequest(false)
}
