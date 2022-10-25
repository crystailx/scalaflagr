package io.github.crystailx.scalaflagr.cache

import io.github.crystailx.scalaflagr.data.EvalResult
import io.github.crystailx.scalaflagr.effect.Applicative


class NoCache[K, F[_]](implicit applicative: Applicative[F]) extends Cacher[K, F] {

  override def set(key: K, evalResult: EvalResult): F[Unit] = applicative.pure(())

  override def get(key: K): F[Option[EvalResult]] = applicative.pure(Option.empty)

  override def invalidate(key: K): F[Unit] = applicative.pure(())

  override def invalidateAll(): F[Unit] = applicative.pure(())
}
