package crystailx.scalaflagr.json.jackson

import crystailx.scalaflagr.json.Encoder

trait EncoderAdapter {
  implicit def encoder[T]: Encoder[T] = mapper.writeValueAsBytes _
}
