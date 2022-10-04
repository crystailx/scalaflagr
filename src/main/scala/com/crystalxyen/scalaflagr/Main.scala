package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.FlagrService.FlagrContext
import com.crystalxyen.scalaflagr.caches.{
  Cacher,
  InMemoryAsyncCacher,
  SimpleCacheKey
}
import com.crystalxyen.scalaflagr.client.SttpClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.higherKinds

object Main {

  implicit class RichFuture[T](val future: Future[T]) extends AnyVal {
    def await: T = Await.result(future, Duration.Inf)
  }

  def main(args: Array[String]): Unit = {
    val client = FlagrClient(
      SttpClient.apply(FlagrEndpoint("http", "localhost", 18000, None))
    )
    import com.crystalxyen.scalaflagr.caches.Implicits._
    implicit val cacher: Cacher[SimpleCacheKey, Future] =
      new InMemoryAsyncCacher[SimpleCacheKey]

//    implicit val ioExecutor: Executor[IO] = Executor.CatsApplicative[IO]
    val flagr = new FlagrService(client)
    val context = FlagrContext("boolean_test")
    println(s"checking flag: $context")
    println(s"is enabled: ${flagr.isEnabled(context).await}")
    println(s"variant: ${flagr.getVariant(context).await}")
    println(s"unsafe variant: ${flagr.getUnsafeVariant(context).await}")
  }
}
