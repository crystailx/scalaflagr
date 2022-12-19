package crystailx.scalaflagr.cache.scaffeine

import com.github.blemale.scaffeine._
import crystailx.scalaflagr.FlagrClient
import crystailx.scalaflagr.data._
import crystailx.scalaflagr.effect.identity._
import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

class CaffeineCacheSpec extends AnyFlatSpec with Matchers {

  import TestData._

  it must "create Scaffeine cache" in {
    val cache: Cache[FlagrContext, EvalResult] =
      Scaffeine().maximumSize(2L).build()
    val cacher = ScaffeineCache(cache)
    cacher.set(key1, value1)
    cacher.get(key1).value mustBe value1
    cacher.underlying.asMap() must have size 1
    cacher.set(key2, value2)
    cacher.get(key2).value mustBe value2
    cacher.underlying.asMap() must have size 2
    cacher.set(key3, value3)
    cacher.get(key3).value mustBe value3
    Await.result(Future(Thread.sleep(500)), 1.second)
    cacher.get(key1) mustBe empty
    cacher.underlying.asMap() must have size 2
  }

  it must "create Scaffeine loading cache" in {
    implicit val client: FlagrClient[Identity] = new FlagrClient[Identity](identityHttpClient)
    val cacher = ScaffeineLoadingCache(Scaffeine().maximumSize(1L), identityEvalResultHandler)
    cacher.get(key1).value mustBe value1
    cacher.underlying.asMap() must have size 1
    cacher.get(key2).value mustBe value2
    Await.result(Future(Thread.sleep(500)), 1.second)
    val underlyingMap = cacher.underlying.asMap()
    underlyingMap must have size 1
    underlyingMap.keys must contain only key2
  }

  it must "create Scaffeine async cache" in {
    import scala.concurrent.ExecutionContext.Implicits.global
    val cache: AsyncCache[FlagrContext, Identity[EvalResult]] =
      Scaffeine().maximumSize(1L).buildAsync[FlagrContext, Identity[EvalResult]]()
    val cacher = ScaffeineAsyncCache(cache)
    noException must be thrownBy Await.result(cacher.set(key1, value1), 500.millis)
    Await.result(Future(Thread.sleep(500 * 5)), 5.second)
    Await.result(cacher.get(key1), 500.millis).value mustBe value1
    cacher.underlying.underlying.asMap() must have size 1
    noException must be thrownBy Await.result(cacher.set(key2, value2), 500.millis)
    Await.result(cacher.get(key2), 500.millis).value mustBe value2
    noException must be thrownBy Await.result(cacher.set(key3, value3), 500.millis)
    Await.result(cacher.get(key3), 500.millis).value mustBe value3
    Await.result(Future(Thread.sleep(500)), 1.second)
    val underlyingMap = cacher.underlying.underlying.asMap()
    underlyingMap must have size 1
    underlyingMap mustNot contain key key1
  }

  it must "create Scaffeine async loading cache" in {
    implicit val client: FlagrClient[Future] = new FlagrClient[Future](futureHttpClient)
    import crystailx.scalaflagr.effect._
    val cacher = ScaffeineAsyncLoadingCache.builder(Scaffeine().maximumSize(2L)).buildAsync
    Await.result(cacher.set(key1, value1), 500.millis)
    Await.result(cacher.get(key1), 500.millis).value mustBe value1
    cacher.underlying.underlying.asMap() must have size 1
    Await.result(cacher.set(key2, value2), 500.millis)
    Await.result(cacher.get(key2), 500.millis).value mustBe value2
    cacher.underlying.underlying.asMap() must have size 2
    Await.result(cacher.set(key3, value3), 500.millis)
    Await.result(cacher.get(key3), 500.millis).value mustBe value3
    Await.result(Future(Thread.sleep(500)), 1.second)
    val underlyingMap = cacher.underlying.underlying.asMap()
    underlyingMap must have size 2
    underlyingMap mustNot contain key key2
  }

  it must "create Scaffeine future async loading cache" in {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val client: FlagrClient[Future] = new FlagrClient[Future](futureHttpClient)
    val cacher = ScaffeineAsyncLoadingCache.builder(Scaffeine().maximumSize(2L)).buildAsyncFuture
    val key1 = BasicContext("flag1")
    val value1 = EvalResult(flagKey = Some("flag1"), timestamp = "", variantAttachment = None)
    Await.result(cacher.set(key1, value1), 500.millis)
    Await.result(cacher.get(key1), 500.millis).value mustBe value1
    cacher.underlying.underlying.asMap() must have size 1
    val key2 = BasicContext("flag2")
    val value2 = EvalResult(flagKey = Some("flag2"), timestamp = "", variantAttachment = None)
    Await.result(cacher.set(key2, value2), 500.millis)
    Await.result(cacher.get(key2), 500.millis).value mustBe value2
    cacher.underlying.underlying.asMap() must have size 2
    val key3 = BasicContext("flag3")
    val value3 = EvalResult(flagKey = Some("flag3"), timestamp = "", variantAttachment = None)
    Await.result(cacher.set(key3, value3), 500.millis)
    Await.result(cacher.get(key3), 500.millis).value mustBe value3
    Await.result(Future(Thread.sleep(500)), 1.second)
    val underlyingMap = cacher.underlying.underlying.asMap()
    underlyingMap must have size 2
    underlyingMap mustNot contain key key2
  }
}
