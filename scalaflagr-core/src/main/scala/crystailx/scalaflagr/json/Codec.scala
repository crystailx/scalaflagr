package crystailx.scalaflagr.json

trait Codec[T] extends Encoder[T] with Decoder[T]
