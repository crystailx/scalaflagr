package com.crystalxyen.scalaflagr.caches

import com.crystalxyen.scalaflagr.model.EvalResult

import scala.language.higherKinds

trait Cacher[CK <: CacheKey[_], F[_]] {
  def set(key: CK, evalResult: EvalResult): F[Unit]
  def get(key: CK): F[Option[EvalResult]]
  def invalidate(key: CK): F[Unit]
  def invalidateAll(): F[Unit]
}
