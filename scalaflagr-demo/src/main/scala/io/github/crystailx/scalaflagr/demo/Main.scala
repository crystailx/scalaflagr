package io.github.crystailx.scalaflagr.demo

import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.semiauto.deriveCodec
import io.github.crystailx.scalaflagr.FlagrService
import io.github.crystailx.scalaflagr.FlagrService.EntityContext
import io.github.crystailx.scalaflagr.cache.nocache._
import io.github.crystailx.scalaflagr.client.{
  FlagrConfig,
  SttpEvaluationClient,
  SttpManagerClient
}
import io.github.crystailx.scalaflagr.json.circe._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object Main extends LazyLogging {

  case class UserInfo(country: String)
  implicit val userInfoCodec: io.circe.Codec[UserInfo] = deriveCodec

  def main(args: Array[String]): Unit = {
    implicit val backend = AkkaHttpBackend.apply()
    val client = new SttpEvaluationClient(FlagrConfig())
    val service = new FlagrService[String, Future](client, new NoCache())
    val flagrContext = EntityContext("flag-key", entityContext = UserInfo("TW"))
    val result = service.isEnabled(flagrContext)
    println(Await.result(result, Duration.Inf))

    val manager = new SttpManagerClient(FlagrConfig())
    val result2 = manager.flag(1L).map(_.key)
    println(Await.result(result2, Duration.Inf))
  }
}
