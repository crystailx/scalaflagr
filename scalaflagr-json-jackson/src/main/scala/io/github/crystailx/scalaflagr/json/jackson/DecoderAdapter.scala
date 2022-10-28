package io.github.crystailx.scalaflagr.json.jackson

import com.fasterxml.jackson.module.scala.JavaTypeable
import io.github.crystailx.scalaflagr.data.RawValue
import io.github.crystailx.scalaflagr.json.Decoder

import scala.util.Try

trait DecoderAdapter {

  implicit def decoder[T: JavaTypeable]: Decoder[T] = new Decoder[T] {
    override def decode(value: RawValue): T = mapper.readValue[T](value)

    override def decodeSafe(value: RawValue): Either[Throwable, T] =
      Try(mapper.readValue[T](value)).toEither
  }
}
