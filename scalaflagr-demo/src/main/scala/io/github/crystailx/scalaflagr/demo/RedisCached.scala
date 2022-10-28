package io.github.crystailx.scalaflagr.demo

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.semiauto.deriveCodec
import io.github.crystailx.scalaflagr.FlagrService
import io.github.crystailx.scalaflagr.cache._
import io.github.crystailx.scalaflagr.cache.redis._
import io.github.crystailx.scalaflagr.client.{
  FlagrConfig,
  SttpEvaluationClient,
  SttpManagerClient
}
import io.github.crystailx.scalaflagr.data.EntityContext
import io.github.crystailx.scalaflagr.json.circe._
import scredis.RedisCluster

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object RedisCached extends LazyLogging {

  case class UserInfo(country: String)
  implicit val userInfoCodec: io.circe.Codec[UserInfo] = deriveCodec

  def main(args: Array[String]): Unit = {
    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.apply()
    val client = new SttpEvaluationClient(FlagrConfig())
    val service = new FlagrService[String, Future](client, new RedisCache(new RedisCluster()))
    val flagrContext = EntityContext("flag-key", UserInfo("TW"))
    val result = service.isEnabled(flagrContext)
    println(Await.result(result, Duration.Inf))

    val manager = new SttpManagerClient(FlagrConfig())
    val result2 = manager.flag(1L).map(_.key)
    println(Await.result(result2, Duration.Inf))
  }
}
