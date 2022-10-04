package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.FlagrDsl._
import com.crystalxyen.scalaflagr.FlagrService.{FlagrContext, Variant}
import com.crystalxyen.scalaflagr.Functor.RichImplicitFunctor
import com.crystalxyen.scalaflagr.Monad.RichImplicitMonad
import com.crystalxyen.scalaflagr.caches.{CacheKey, CacheKeyCreator, Cacher}
import com.crystalxyen.scalaflagr.model.EvalResult
import com.typesafe.scalalogging.LazyLogging
import io.circe.{Decoder, Json}

import scala.language.{higherKinds, postfixOps}

class FlagrService[CK <: CacheKey[_], F[_]](client: FlagrClient)(
    implicit
    keyCreator: CacheKeyCreator[CK],
    cacher: Cacher[CK, F],
    functor: Functor[F],
    monad: Monad[F],
    applicative: Applicative[F],
    executor: Executor[F]
) extends LazyLogging {

  protected def evaluate(
      context: FlagrContext
  ): F[EvalResult] = {
    import context._
    val key = keyCreator(flagKey, entityID, entityType, entityContext)
    cacher.get(key) flatMap { evalResult =>
      def fetcher: F[EvalResult] =
        client
          .execute(postEvaluation(flagKey, entityID, entityType, entityContext))
          .map { res =>
            cacher.set(key, res.result)
            res.result
          }
      evalResult.fold(fetcher)(applicative.pure)
    }
  }

  def isEnabled(
      context: FlagrContext
  ): F[Boolean] = {
    val result = evaluate(context)
    result.map { eval =>
      (eval.flagKey contains context.flagKey) && (eval.segmentID nonEmpty) // There'll always be an segment if flag is enabled, even when the segment constraint doesn't match
    }
  }

  def getVariant(
      context: FlagrContext
  ): F[Option[Variant]] =
    evaluate(context) map { eval =>
      eval.variantKey
        .filter(_ => eval.flagKey contains context.flagKey)
        .map(key => Variant(key, eval.variantAttachment))
    }

  def getUnsafeVariant(
      context: FlagrContext
  ): F[Variant] =
    getVariant(context).map(
      _.getOrElse(throw new Exception("No matched variant"))
    )

}

object FlagrService {

  case class FlagrContext(
      flagKey: String,
      entityID: String = "anonymous",
      entityType: String = "user",
      entityContext: Json = Json.Null
  )

  case class Variant(key: String, attachments: Option[Json]) {

    def asAttachments[T: Decoder]: Option[T] =
      attachments.flatMap(_.as[T].toOption)
  }
}
