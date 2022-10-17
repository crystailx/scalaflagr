package com.crystailx.scalaflagr

import com.crystailx.scalaflagr.FlagrService.FlagrContext
import com.crystailx.scalaflagr.cache.{ CacheKeyCreator, Cacher }
import com.crystailx.scalaflagr.client.EvaluationClient
import com.crystailx.scalaflagr.data.{ EvalContext, EvalResult, Variant }
import com.crystailx.scalaflagr.effect.{
  Applicative,
  Functor,
  Monad,
  RichImplicitFunctor,
  RichImplicitMonad
}
import com.crystailx.scalaflagr.json.{ Decoder, Encoder }
import com.typesafe.scalalogging.LazyLogging

import scala.language.postfixOps

class FlagrService[K, F[_]](client: EvaluationClient[F])(implicit
  keyCreator: CacheKeyCreator[K],
  cacher: Cacher[K, F],
  functor: Functor[F],
  monad: Monad[F],
  applicative: Applicative[F],
  encoder: Encoder[EvalContext],
  decoder: Decoder[EvalResult]
) extends LazyLogging {

  protected def createEvalContext(
    context: FlagrContext
  ): EvalContext = EvalContext(
    Some(context.entityID),
    Some(context.entityType),
    context.entityContext,
    flagKey = Some(context.flagKey)
  )

  protected def evaluate(
    context: FlagrContext
  ): F[EvalResult] = {
    import context._
    val key = keyCreator(flagKey, entityID, entityType, entityContext)
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

  def isEnabled(context: FlagrContext): F[Boolean] = {
    val result = evaluate(context)
    result.map { eval =>
      (eval.flagKey contains context.flagKey) && (eval.segmentID nonEmpty) // There'll always be a segment if the flag is enabled, even when the segment constraint doesn't match
    }
  }

  def getVariant(context: FlagrContext): F[Option[Variant]] =
    evaluate(context) map { eval =>
      eval.variantKey
        .filter(_ => eval.flagKey contains context.flagKey)
        .flatMap(_ => eval.toVariant)
    }

  def getUnsafeVariant(context: FlagrContext): F[Variant] =
    getVariant(context).map(
      _.getOrElse(throw new Exception("No matched variant"))
    )

}

object FlagrService {

  sealed trait FlagrContext {
    val flagKey: String

    val entityID: String

    val entityType: String

    val entityContext: Option[String]
  }

  case class BasicContext(
    flagKey: String,
    override val entityID: String = "anonymous",
    override val entityType: String = "user"
  ) extends FlagrContext {
    override val entityContext: Option[String] = None
  }

  case class EntityContext(
    flagKey: String,
    override val entityID: String,
    override val entityType: String,
    override val entityContext: Option[String]
  ) extends FlagrContext

  object EntityContext {

    def apply[T](
      flagKey: String,
      entityID: String = "anonymous",
      entityType: String = "user",
      entityContext: T
    )(implicit
      encoder: Encoder[T]
    ): EntityContext =
      new EntityContext(flagKey, entityID, entityType, Some(encoder.encode(entityContext)))
  }

}
