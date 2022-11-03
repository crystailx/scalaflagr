package crystailx.scalaflagr

import crystailx.scalaflagr.client.HttpClient
import crystailx.scalaflagr.effect.Functor
import crystailx.scalaflagr.json.{ Decoder, Encoder }

case class FlagrClient[F[_]](httpClient: HttpClient[F]) {

  def executeSafe[T, R](
    requestHandler: RequestHandler[T, R]
  )(implicit
    functor: Functor[F],
    encoder: Encoder[T],
    decoder: Decoder[R]
  ): F[Either[Throwable, R]] =
    functor.map(httpClient.send(requestHandler.build))(requestHandler.handle)

  def execute[T, R](
    requestHandler: RequestHandler[T, R]
  )(implicit
    functor: Functor[F],
    encoder: Encoder[T],
    decoder: Decoder[R]
  ): F[R] =
    functor.map(httpClient.send(requestHandler.build))(
      requestHandler.handle(_).fold(throw _, identity[R])
    )

}
