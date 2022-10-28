package io.github.crystailx.scalaflagr.json.jackson

import io.github.crystailx.scalaflagr.json.Encoder

trait EncoderAdapter {
  implicit def encoder[T]: Encoder[T] = mapper.writeValueAsBytes _
}
