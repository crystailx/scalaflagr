package com.crystailx.scalaflagr.demo

import com.crystailx.scalaflagr.FlagrService
import com.crystailx.scalaflagr.FlagrService.EntityContext
import com.crystailx.scalaflagr.client.{ FlagrConfig, SttpEvaluationClient }
import com.crystailx.scalaflagr.json.circe._
import com.crystailx.scalaflagr.syntax._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.semiauto.deriveCodec

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main extends LazyLogging {

  case class UserInfo(country: String)
  implicit val userInfoCodec: io.circe.Codec[UserInfo] = deriveCodec

  def main(args: Array[String]): Unit = {
    implicit val backend = AkkaHttpBackend.apply()
    val client = new SttpEvaluationClient(FlagrConfig())
    val service = new FlagrService(client)
    val flagrContext = EntityContext("flag-key", entityContext = UserInfo("TW"))
    val result = service.isEnabled(flagrContext)
    println(Await.result(result, Duration.Inf))
  }
}
