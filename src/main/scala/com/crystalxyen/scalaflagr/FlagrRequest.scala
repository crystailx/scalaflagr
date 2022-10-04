package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.client.HttpEntity
import com.crystalxyen.scalaflagr.client.HttpEntity.StringEntity

case class FlagrRequest(
    method: String,
    endpoint: String,
    params: Map[String, String],
    entity: Option[HttpEntity],
    headers: Map[String, String]
) {

  def addParameter(name: String, value: String): FlagrRequest =
    copy(params = params + (name -> value))
}

object FlagrRequest {

  def apply(method: String, endpoint: String): FlagrRequest =
    apply(method, endpoint, Map.empty[String, Any])

  def apply(method: String, endpoint: String, body: HttpEntity): FlagrRequest =
    apply(method, endpoint, Map.empty[String, Any], body)

  def apply(
      method: String,
      endpoint: String,
      params: Map[String, Any]
  ): FlagrRequest =
    apply(method, endpoint, params.mapValues(_.toString), None, Map.empty)

  def apply(
      method: String,
      endpoint: String,
      params: Map[String, Any],
      body: HttpEntity
  ): FlagrRequest =
    apply(method, endpoint, params.mapValues(_.toString), Some(body), Map.empty)

  implicit val FlagrRequestShow: Show[FlagrRequest] = (t: FlagrRequest) => {
    val queryParams = t.params.map { case (k, v) => k + "=" + v }.mkString("&")
    val headers = t.headers.map { case (k, v)    => k + ": " + v }.mkString("\n")
    val header = s"${t.method} ${t.endpoint}?$queryParams".stripSuffix("?")
    t.entity.fold(header) { body =>
      val content = body match {
        case StringEntity(content, _) => content
        case _                        => body
      }
      s"$headers\n$header\n$content"
    }
  }
}
