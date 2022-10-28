package io.github.crystailx.scalaflagr.client

import io.github.crystailx.scalaflagr.data.RawValue
import io.github.crystailx.scalaflagr.effect.Functor
import io.github.crystailx.scalaflagr.json.{ Decoder, Encoder }
import shapeless.ops.hlist.ToTraversable
import shapeless.{ HList, LabelledGeneric }
import shapeless.ops.record.Keys

package object api extends ParamMapBuilder {

  /*private[api] def buildParamsMap[T <: Product](params: T): Map[String, String] =
    params.productElementNames
      .zip(params.productIterator)
      .filterNot(_._2 == None)
      .map { kv =>
        kv._1 -> (kv._2 match {
          case value: Option[_] =>
            value.fold("")(_.toString)
          case value => value.toString
        })
      }
      .toMap

  private[api] def buildParamsMap2[T <: Product, L <: HList, R <: HList](params: T)(implicit
    gen: LabelledGeneric.Aux[T, L],
    keys: Keys.Aux[L, R],
    ts: ToTraversable.Aux[R, List, Symbol]
  ): Map[String, String] = {
    val fieldNames = keys().toList.map(_.name)
    val values = params.productIterator.toList.map(_.toString)
    Map(fieldNames zip values: _*)
  }*/

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
