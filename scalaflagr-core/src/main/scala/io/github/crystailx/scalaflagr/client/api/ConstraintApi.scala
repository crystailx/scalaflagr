package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.implicitConversions

trait ConstraintApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig

  import config._

  private val apiBasePath: String = s"$host$basePath/flags/%d/segments/%d/constraints"

  def createConstraint(flagID: Long, segmentID: Long, body: CreateConstraintRequest)(implicit
    encoder: Encoder[CreateConstraintRequest],
    decoder: Decoder[Constraint]
  ): F[Constraint] =
    post(apiBasePath format (flagID, segmentID), body)

  def deleteConstraint(flagID: Long, segmentID: Long, constraintID: Long): F[Unit] =
    delete(s"$apiBasePath/$constraintID" format (flagID, segmentID))

  def findConstraints(flagID: Long, segmentID: Long)(implicit
    decoder: Decoder[List[Constraint]]
  ): F[List[Constraint]] =
    get(apiBasePath format (flagID, segmentID))

  def updateConstraint(
    flagID: Long,
    segmentID: Long,
    constraintID: Long,
    body: UpdateConstraintRequest
  )(implicit
    encoder: Encoder[UpdateConstraintRequest],
    decoder: Decoder[Constraint]
  ): F[Constraint] =
    put(s"$apiBasePath/$constraintID" format (flagID, segmentID), body)
}
