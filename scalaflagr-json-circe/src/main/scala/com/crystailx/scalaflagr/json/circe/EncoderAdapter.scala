package com.crystailx.scalaflagr.json.circe

import com.crystailx.scalaflagr.data.EvalContext
import com.crystailx.scalaflagr.json.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ Encoder => CirceEncoder }

trait EncoderAdapter {

  implicit def encoder[T](implicit encoder: CirceEncoder[T]): Encoder[T] =
    (body: T) => encoder.apply(body).noSpaces

  // some default encoders to avoid boilerplate
  implicit val evalContextCirceEncoder: CirceEncoder[EvalContext] = deriveEncoder
}
