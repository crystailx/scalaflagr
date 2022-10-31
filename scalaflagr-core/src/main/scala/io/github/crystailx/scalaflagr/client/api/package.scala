package io.github.crystailx.scalaflagr.client

import io.github.crystailx.scalaflagr.data.RawValue
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }

package object api extends ParamMapBuilder {

  private[api] implicit def autoFunctorDecoderMapping[T, F[_]](
    res: F[RawValue]
  )(implicit functor: Functor[F], decoder: Decoder[T]): F[T] =
    functor.map(res)(decoder.decode)

  private[api] implicit def autoFunctorUnitMapping[F[_]](res: F[RawValue])(implicit
    functor: Functor[F]
  ): F[Unit] =
    functor.map(res)(_ => ())

  private[api] implicit def autoEncode[T](value: T)(implicit encoder: Encoder[T]): RawValue =
    encoder.encode(value)
}
