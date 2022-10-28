package io.github.crystailx.scalaflagr.json

import io.github.crystailx.scalaflagr.data.RawValue

trait Encoder[T] {
  def encode(body: T): RawValue
}
