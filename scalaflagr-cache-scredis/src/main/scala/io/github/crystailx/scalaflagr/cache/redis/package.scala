package io.github.crystailx.scalaflagr.cache

import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }
import scredis.serialization.{ Reader, UTF8StringReader, UTF8StringWriter, Writer }

package object redis {

  implicit def redisWriter[T](implicit encoder: Encoder[T]): Writer[T] = (value: T) =>
    UTF8StringWriter.write(encoder.encode(value))

  implicit def redisReader[T](implicit decoder: Decoder[T]): Reader[T] = (bytes: Array[Byte]) =>
    decoder.decode(UTF8StringReader.read(bytes))
}
