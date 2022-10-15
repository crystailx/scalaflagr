package com.crystailx.scalaflagr.cache

import com.crystailx.scalaflagr.data.EvalResult


trait Cacher[K, F[_]] {
  def set(key: K, evalResult: EvalResult): F[Unit]
  def get(key: K): F[Option[EvalResult]]
  def invalidate(key: K): F[Unit]
  def invalidateAll(): F[Unit]
}
