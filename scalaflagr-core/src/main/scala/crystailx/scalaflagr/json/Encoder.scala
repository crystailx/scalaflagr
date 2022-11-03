package crystailx.scalaflagr.json

import crystailx.scalaflagr.data.RawValue

trait Encoder[T] {
  def encode(body: T): RawValue
}
