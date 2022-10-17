package com.crystailx.scalaflagr.cache

import com.crystailx.scalaflagr.data.EvalResult
import com.crystailx.scalaflagr.effect.Applicative


class NoCache[K, F[_]](implicit applicative: Applicative[F]) extends Cacher[K, F] {

  override def set(key: K, evalResult: EvalResult): F[Unit] = applicative.pure(())

  override def get(key: K): F[Option[EvalResult]] = applicative.pure(Option.empty)

  override def invalidate(key: K): F[Unit] = applicative.pure(())

  override def invalidateAll(): F[Unit] = applicative.pure(())
}
