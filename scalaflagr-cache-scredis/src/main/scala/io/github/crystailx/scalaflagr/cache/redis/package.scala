package io.github.crystailx.scalaflagr.cache

import io.github.crystailx.scalaflagr.data.RawValue
import io.github.crystailx.scalaflagr.json.{Decoder, Encoder}
import scredis.serialization.{BytesReader, BytesWriter, Reader, Writer}

package object redis {

  implicit def redisWriter[T](implicit encoder: Encoder[T]): Writer[T] = (value: T) =>
    BytesWriter.write(encoder.encode(value))

  implicit def redisReader[T](implicit decoder: Decoder[T]): Reader[T] = (bytes: RawValue) =>
    decoder.decode(BytesReader.read(bytes))
}
