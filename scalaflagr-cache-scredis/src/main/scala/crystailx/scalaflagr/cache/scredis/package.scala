package crystailx.scalaflagr.cache

import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.json.{ Decoder, Encoder }
import _root_.scredis.serialization.{ BytesReader, BytesWriter, Reader, Writer }

package object scredis {

  implicit def redisWriter[T](implicit encoder: Encoder[T]): Writer[T] = (value: T) =>
    BytesWriter.write(encoder.encode(value))

  implicit def redisReader[T](implicit decoder: Decoder[T]): Reader[T] = (bytes: RawValue) =>
    decoder.decode(BytesReader.read(bytes))
}
