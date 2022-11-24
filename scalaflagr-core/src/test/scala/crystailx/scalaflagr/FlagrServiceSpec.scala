package crystailx.scalaflagr

import com.typesafe.scalalogging.LazyLogging
import crystailx.scalaflagr.cache.{ CacheKeyCreator, Cacher }
import crystailx.scalaflagr.client.HttpClient
import crystailx.scalaflagr.data.{ BasicContext, EvalContext, EvalResult, RawValue }
import crystailx.scalaflagr.effect.{ Applicative, Functor, Monad }
import crystailx.scalaflagr.json.{ Decoder, Encoder }
import org.scalatest.OptionValues.convertOptionToValuable
import org.scalatest.Outcome
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.collection.mutable

class FlagrServiceSpec extends FixtureAnyFlatSpec with Matchers with LazyLogging {

  case class FixtureParam() {

    val cacher: Cacher[String, Option] = new Cacher[String, Option] {
      private val cache: mutable.HashMap[String, EvalResult] = mutable.HashMap.empty

      override def set(key: String, evalResult: EvalResult): Option[Unit] = {
        logger.debug(s"set cache, key: $key, val: $evalResult")
        cache.put(key, evalResult).map(_ => ())
      }

      override def get(key: String): Option[Option[EvalResult]] = {
        val result = cache.get(key)
        logger.debug(s"get cache, key: $key, val: $result")
        Some(result)
      }

      override def invalidate(key: String): Option[Unit] = {
        logger.debug(s"invalidate cache, key: $key")
        cache.remove(key).map(_ => ())
      }

      override def invalidateAll(): Option[Unit] = {
        logger.debug("invalidate all caches")
        Some(cache.clear())
      }
    }

    implicit val cacheKeyCreator: CacheKeyCreator[String] =
      (v1: String, v2: String, v3: String, v4: Option[RawValue]) => {
        val key = s"$v1-$v2-$v3-${v4.map(new String(_).take(10)).getOrElse("")}"
        logger.debug(s"created key: $key")
        key
      }

    implicit val optFunctor: Functor[Option] = new Functor[Option] {
      override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
    }

    implicit val optMonad: Monad[Option] = new Monad[Option] {
      override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
    }

    implicit val optApplicative: Applicative[Option] = new Applicative[Option] {
      override def pure[A](value: A): Option[A] = Option(value)
    }
    implicit val encoder: Encoder[EvalContext] = _.toString.getBytes

    implicit val decoder: Decoder[EvalResult] = new Decoder[EvalResult] {

      override def decode(value: RawValue): EvalResult = {
        logger.debug(s"decoding body: ${new String(value)}")
        EvalResult(
          flagKey = Some("matched"),
          segmentID = Some(1),
          variantKey = Some("on"),
          variantAttachment = Some(value),
          timestamp = "2022-10-31T16:30:14Z"
        )
      }

      override def decodeSafe(value: RawValue): Either[Throwable, EvalResult] = Right(
        decode(value)
      )
    }

    val httpClient: HttpClient[Option] = new HttpClient[Option] {

      private val response: String =
        """
          |{
          | "flagKey": "matched",
          | "segmentID": 1
          |}
          |""".stripMargin
      override protected val config: FlagrConfig = FlagrConfig("http://localhost:12345")

      override def send(request: FlagrRequest): Option[RawValue] = {
        import request._
        logger.debug(s"$path\n$body")
        Some(response.getBytes)
      }
    }
    val client: FlagrClient[Option] = new FlagrClient[Option](httpClient)
  }

  override protected def withFixture(test: OneArgTest): Outcome =
    test(FixtureParam())

  it must "create new flagr service with implicits" in { f =>
    import f._
    noException must be thrownBy new FlagrService(client, cacher)
  }

  it must "check flag is enabled" in { f =>
    import f._
    val service = new FlagrService(client, cacher)
    service.isEnabled(BasicContext("matched")).value mustBe true
    service.isEnabled(BasicContext("unmatched")).value mustBe false
  }

  it must "get matched flag variants" in { f =>
    import f._
    val service = new FlagrService(client, cacher)
    an[Exception] must be thrownBy service.getUnsafeVariant(BasicContext("matched"))
  }

}
