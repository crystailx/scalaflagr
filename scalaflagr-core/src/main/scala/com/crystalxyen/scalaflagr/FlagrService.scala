package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.FlagrService.{FlagrContext, Variant}
import com.crystalxyen.scalaflagr.cache.CacheKey.CacheKeyCreator
import com.crystalxyen.scalaflagr.cache.{CacheKey, Cacher}
import com.crystalxyen.scalaflagr.client.EvaluationClient
import com.crystalxyen.scalaflagr.data.{EvalContext, EvalResult}
import com.crystalxyen.scalaflagr.syntax._
import com.typesafe.scalalogging.LazyLogging
import io.circe.{Decoder, Json}

import scala.language.{higherKinds, postfixOps}

class FlagrService[K, CK <: CacheKey[K], F[_]](client: EvaluationClient[F])(
    implicit
    keyCreator: CacheKeyCreator[CK],
    cacher: Cacher[K, F],
    functor: Functor[F],
    monad: Monad[F],
    applicative: Applicative[F]
) extends LazyLogging {

  def createEvalContext(context: FlagrContext): EvalContext = EvalContext(
    Some(context.entityID),
    Some(context.entityType),
    Option(context.entityContext).filterNot(_.isNull),
    flagKey = Some(context.flagKey)
  )

  protected def evaluate(
      context: FlagrContext
  ): F[EvalResult] = {
    import context._
    val key = keyCreator.create(flagKey, entityID, entityType, entityContext)
    cacher.get(key) flatMap { evalResult =>
      def fetcher: F[EvalResult] =
        client
          .evaluate(createEvalContext(context))
          .map { res =>
            cacher.set(key, res)
            res
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
