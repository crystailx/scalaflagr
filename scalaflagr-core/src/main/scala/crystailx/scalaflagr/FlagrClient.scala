package crystailx.scalaflagr

import crystailx.scalaflagr.client.HttpClient
import crystailx.scalaflagr.effect.Functor
import crystailx.scalaflagr.json.{ Decoder, Encoder }

case class FlagrClient[F[_]](httpClient: HttpClient[F]) {

  def execute[T, R](
    requestBuilder: RequestBuilder[T]
  )(implicit
    functor: Functor[F],
    encoder: Encoder[T],
    decoder: Decoder[R]
  ): F[R] =
    functor.map(httpClient.send(requestBuilder.build))(decoder.decode)

}
