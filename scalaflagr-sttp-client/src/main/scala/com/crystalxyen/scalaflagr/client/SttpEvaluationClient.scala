package com.crystalxyen.scalaflagr.client

import com.crystalxyen.scalaflagr.data.{
  EvalContext,
  EvalResult,
  EvaluationBatchRequest,
  EvaluationBatchResponse
}
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe._

import scala.concurrent.{ExecutionContext, Future}

class SttpEvaluationClient(host: String, basePath: String = "/api/v1")(
    implicit
    sttpBackend: SttpBackend[Future, Nothing],
    ec: ExecutionContext
) extends EvaluationClient[Future] {

  type SttpCirceResponse[T] =
    Response[Either[DeserializationError[Error], T]]

  implicit val evalContextCodec: Codec[EvalContext] = derivation.deriveCodec

  implicit val evalResultCodec: Codec[EvalResult] = derivation.deriveCodec

  implicit val evaluationBatchRequestCodec: Codec[EvaluationBatchRequest] =
    derivation.deriveCodec

  implicit val evaluationBatchResponseCodec: Codec[EvaluationBatchResponse] =
    derivation.deriveCodec

  private def genericResponseHandler[T](response: SttpCirceResponse[T]): T =
    response.body
      .fold(s => throw new Exception(s), _.fold(e => throw e.error, identity))

  protected def sender[T: Encoder, U: Decoder](
      path: String,
      body: T
  ): Future[U] =
    sttp
      .post(uri"$host$basePath$path")
      .body(body)
      .response(asJson[U])
      .send()
      .map(genericResponseHandler)

  override def evaluate(context: EvalContext): Future[EvalResult] =
    sender("/evaluation", context)

  override def batchEvaluate(
      context: EvaluationBatchRequest
  ): Future[EvaluationBatchResponse] =
    sender("/evaluation/batch", context)
}
