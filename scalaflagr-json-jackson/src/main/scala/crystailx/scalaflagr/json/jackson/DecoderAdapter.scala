package crystailx.scalaflagr.json.jackson

import com.fasterxml.jackson.module.scala.JavaTypeable
import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.json.Decoder

import scala.util.Try

trait DecoderAdapter {

  implicit def decoderAdapter[T: JavaTypeable]: Decoder[T] = new Decoder[T] {
    override def decode(value: RawValue): T = mapper.readValue[T](value)

    override def decodeSafe(value: RawValue): Either[Throwable, T] =
      Try(mapper.readValue[T](value)).toEither
  }
}
