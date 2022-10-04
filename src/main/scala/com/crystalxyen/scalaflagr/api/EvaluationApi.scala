package com.crystalxyen.scalaflagr.api

import com.crystalxyen.scalaflagr.model.{EvalContext, EvalResult}
import io.circe.Json

import scala.concurrent.Future

trait EvaluationApi {

  def postEvaluation(flagKey: String, entityID: String): EvalContext =
    EvalContext(Some(entityID), flagKey = Some(flagKey))

  def postEvaluation(
      flagKey: String,
      entityID: String,
      entityContext: Json
  ): EvalContext =
    EvalContext(
      Some(entityID),
      entityContext = Some(entityContext),
      flagKey = Some(flagKey)
    )

  def postEvaluation(
      flagKey: String,
      entityID: String,
      entityType: String
  ): EvalContext =
    EvalContext(Some(entityID), Some(entityType), flagKey = Some(flagKey))

  def postEvaluation(
      flagKey: String,
      entityID: String,
      entityType: String,
      entityContext: Json
  ): EvalContext =
    EvalContext(
      Some(entityID),
      Some(entityType),
      Some(entityContext),
      flagKey = Some(flagKey)
    )
}
