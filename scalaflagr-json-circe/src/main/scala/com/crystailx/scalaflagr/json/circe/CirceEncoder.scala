package com.crystailx.scalaflagr.json.circe

import com.crystailx.scalaflagr.data.EvalContext
import com.crystailx.scalaflagr.json.Encoder
import io.circe.derivation.deriveEncoder

trait CirceEncoder {

  implicit def encoder[T](implicit encoder: io.circe.Encoder[T]): Encoder[T] =
    (body: T) => encoder.apply(body).noSpaces

  implicit val evalContextCirceEncoder: io.circe.Encoder[EvalContext] = deriveEncoder
}
