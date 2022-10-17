package com.crystailx.scalaflagr.json

trait Decoder[T] {
  def decode(body: String): T

  def decodeSafe(body: String): Either[Exception, T]
}
