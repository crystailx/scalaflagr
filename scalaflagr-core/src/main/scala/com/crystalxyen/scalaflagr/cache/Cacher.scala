package com.crystalxyen.scalaflagr.cache

import com.crystalxyen.scalaflagr.data.EvalResult

import scala.language.higherKinds

trait Cacher[K, F[_]] {
  def set(key: CacheKey[K], evalResult: EvalResult): F[Unit]
  def get(key: CacheKey[K]): F[Option[EvalResult]]
  def invalidate(key: CacheKey[K]): F[Unit]
  def invalidateAll(): F[Unit]
}
