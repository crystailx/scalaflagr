package io.github.crystailx.scalaflagr.json

import io.github.crystailx.scalaflagr.data.RawValue

trait Decoder[T] {
  def decode(value: RawValue): T

  def decodeSafe(value: RawValue): Either[Throwable, T]
}
