package io.github.crystailx.scalaflagr

import com.typesafe.scalalogging.LazyLogging
import io.github.crystailx.scalaflagr.cache.{CacheKeyCreator, Cacher}
import io.github.crystailx.scalaflagr.client.EvaluationClient
import io.github.crystailx.scalaflagr.data.{EvalContext, EvalResult, FlagrContext, Variant}
import io.github.crystailx.scalaflagr.effect.{Applicative, Functor, Monad, RichImplicitFunctor, RichImplicitMonad}
import io.github.crystailx.scalaflagr.json.{Decoder, Encoder}

import scala.language.{existentials, postfixOps}

class FlagrService[K, F[_]](client: EvaluationClient[F], cacher: Cacher[K, F])(implicit
  keyCreator: CacheKeyCreator[K],
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
