package crystailx.scalaflagr.json.jackson

import crystailx.scalaflagr.json.Encoder

trait EncoderAdapter {
  implicit def encoderAdapter[T]: Encoder[T] = mapper.writeValueAsBytes _
}
