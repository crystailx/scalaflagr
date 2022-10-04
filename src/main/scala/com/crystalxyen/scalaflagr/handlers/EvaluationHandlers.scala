package com.crystalxyen.scalaflagr.handlers

import com.crystalxyen.scalaflagr._
import com.crystalxyen.scalaflagr.client.{HttpEntity, HttpResponse}
import com.crystalxyen.scalaflagr.model.{
  evalContextCodec,
  evalResultCodec,
  EvalContext,
  EvalResult
}
import io.circe.syntax._
import io.circe.{Decoder, Json}

trait EvaluationHandlers {

  implicit object EvaluationHandler extends Handler[EvalContext, EvalResult] {

    override def responseHandler: ResponseHandler[EvalResult] =
      new ResponseHandler[EvalResult] {

        override def handle(
            response: HttpResponse
        )(
            implicit
            decoder: Decoder[EvalResult]
        ): Either[FlagrError, EvalResult] = {

          def bad(status: Int): Left[FlagrError, EvalResult] = {
            val node = ResponseHandler.fromResponse[Json](response)
            if (node.hcursor.downField("error").focus.exists(_.isObject))
              Left(FlagrErrorParser.parse(response))
            else
              Left(
                FlagrError(
                  response.entity.get.content,
                  response.entity.get.content,
                  None,
                  None,
                  None,
                  Nil,
                  None
                )
              )
          }

          def good: Right[Nothing, EvalResult] = Right(
            ResponseHandler.fromResponse[EvalResult](response)
          )

          response.statusCode match {
            case 200 => good
            // 404s are odd, can be different document types
            case 404 =>
              val node = ResponseHandler.fromResponse[Json](response)
              if (node.hcursor.downField("error").focus.nonEmpty) bad(404)
              else good
            case other => bad(other)
          }
        }
      }

    override def build(request: EvalContext): FlagrRequest = {
      val endpoint = s"$basePath/evaluation"
      val params = scala.collection.mutable.Map.empty[String, String]
      val body = request.asJson.noSpaces
      FlagrRequest(
        "POST",
        endpoint,
        params.toMap,
        HttpEntity(body, "application/json")
      )
    }
  }
}
