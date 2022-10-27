package io.github.crystailx.scalaflagr.client.api

import io.github.crystailx.scalaflagr.client.{ FlagrConfig, HttpClient }
import io.github.crystailx.scalaflagr.data._
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

import scala.language.implicitConversions

trait SegmentApi[F[_]] extends HttpClient[F] {
  protected implicit val functor: Functor[F]
  protected val config: FlagrConfig

  import config._

  private val apiBasePath: String = s"$host$basePath/flags/%d/segments"

  def createSegment(flagID: Long, body: CreateSegmentRequest)(implicit
    encoder: Encoder[CreateSegmentRequest],
    decoder: Decoder[Segment]
  ): F[Segment] =
    post(apiBasePath format flagID, body)

  def deleteSegment(flagID: Long, segmentID: Long): F[Unit] =
    delete(s"$apiBasePath/$segmentID" format flagID)

  def findSegments(flagID: Long)(implicit decoder: Decoder[List[Segment]]): F[List[Segment]] =
    get(apiBasePath format flagID)

  def updateSegment(flagID: Long, segmentID: Long, body: UpdateSegmentRequest)(implicit
    encoder: Encoder[UpdateSegmentRequest],
    decoder: Decoder[Segment]
  ): F[Segment] =
    put(s"$apiBasePath/$segmentID" format flagID, body)

  def reorderSegments(flagID: Long, body: ReorderSegmentRequest)(implicit
    encoder: Encoder[ReorderSegmentRequest]
  ): F[Unit] =
    put(s"$apiBasePath/reorder" format flagID, body)

}
