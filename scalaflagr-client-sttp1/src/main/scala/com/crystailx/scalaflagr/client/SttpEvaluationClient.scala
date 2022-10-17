package com.crystailx.scalaflagr.client

import com.softwaremill.sttp._

import scala.concurrent.{ ExecutionContext, Future }

class SttpEvaluationClient(override val config: FlagrConfig)(implicit
  sttpBackend: SttpBackend[Future, Nothing],
  ec: ExecutionContext
) extends EvaluationClient[Future] {

  override protected def send(url: String, body: String): Future[String] =
    sttp
      .post(uri"$url")
      .body(body)
      .contentType(MediaTypes.Json)
      .response(asString)
      .send()
      .map(_.body.fold(s => throw new Exception(s), identity))

}
