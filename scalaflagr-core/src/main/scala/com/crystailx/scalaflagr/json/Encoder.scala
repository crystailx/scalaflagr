package com.crystailx.scalaflagr.json

trait Encoder[T] {
  def encode(body: T): String
}
