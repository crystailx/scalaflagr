package io.github.crystailx.scalaflagr.demo

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.typesafe.scalalogging.LazyLogging
import io.github.crystailx.scalaflagr.FlagrService
import io.github.crystailx.scalaflagr.client.{
  FlagrConfig,
  SttpEvaluationClient,
  SttpManagerClient
}
import io.github.crystailx.scalaflagr.data.{ BasicContext, CreateTagRequest, EntityContext }
import play.api.libs.json.Writes
import scredis.RedisCluster

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object Main extends LazyLogging {

  case class UserInfo(country: String)

  def main(args: Array[String]): Unit = {
    noCacheSttpCirceEvalSvc()
    sttpCirceMngClient()
    //redisCacheSttpCirceEvalSvc()
    noCacheSttpPlayJsonEvalSvc()
  }

  def noCacheSttpCirceEvalSvc(): Unit = {
    import io.circe.generic.semiauto.deriveCodec
    import io.github.crystailx.scalaflagr.cache.nocache._
    import io.github.crystailx.scalaflagr.json.circe._

    implicit val userInfoCodec: io.circe.Codec[UserInfo] = deriveCodec
    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.apply()

    val client = new SttpEvaluationClient(FlagrConfig())
    val service = new FlagrService[String, Future](client, new NoCache())
    val flagrContext = EntityContext("flag-key", UserInfo("TW"))
    val result = service.isEnabled(flagrContext)
    println(Await.result(result, Duration.Inf))
    val varResult = service.getVariant(BasicContext("flag-key"))
    println(Await.result(varResult, Duration.Inf))
  }

  def sttpCirceMngClient(): Unit = {
    import io.github.crystailx.scalaflagr.json.circe._
    io.circe.Decoder.decodeLocalDateTime
    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.apply()

    val manager = new SttpManagerClient(FlagrConfig())
    import io.github.crystailx.scalaflagr.client.syntax._
    manager.createTag(1L, createTagRequest value "new tag")
    val result2 = manager.flag(1L).map(_.key)
    println(Await.result(result2, Duration.Inf))
  }

  def redisCacheSttpCirceEvalSvc(): Unit = {
    import io.circe.generic.auto._
    import io.github.crystailx.scalaflagr.cache.redis._
    import io.github.crystailx.scalaflagr.cache.simpleCacheKey
    import io.github.crystailx.scalaflagr.json.circe._

    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.apply()

    val client = new SttpEvaluationClient(FlagrConfig())
    val service = new FlagrService[String, Future](client, new RedisCache(new RedisCluster()))
    val flagrContext = EntityContext("flag-key", UserInfo("TW"))
    val rResult = service.isEnabled(flagrContext)
    println(Await.result(rResult, Duration.Inf))
    val rVarResult = service.getVariant(BasicContext("flag-key"))
    println(Await.result(rVarResult, Duration.Inf))
  }

  def noCacheSttpPlayJsonEvalSvc(): Unit = {
    import io.github.crystailx.scalaflagr.cache.nocache._
    import io.github.crystailx.scalaflagr.json.play._
    import play.api.libs.json.Json
    implicit val backend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.apply()
    implicit val userInfoEncoder: Writes[UserInfo] = Json.writes[UserInfo]
    val client = new SttpEvaluationClient(FlagrConfig())
    val service = new FlagrService[String, Future](client, new NoCache)
    val flagrContext = EntityContext("flag-key", UserInfo("TW"))
    val rResult = service.isEnabled(flagrContext)
    println(Await.result(rResult, Duration.Inf))
    val rVarResult = service.getVariant(BasicContext("flag-key"))
    println(Await.result(rVarResult, Duration.Inf))
  }
}
