package com.crystalxyen.scalaflagr

import cats.effect.Async
import com.crystalxyen.scalaflagr.client.{HttpClient, HttpResponse}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.higherKinds

trait Executor[F[_]] {
  def exec(client: HttpClient, request: FlagrRequest): F[HttpResponse]
}

object Executor {

  def apply[F[_]: Executor](): Executor[F] = implicitly[Executor[F]]

  implicit def FutureExecutor(
      implicit
      ec: ExecutionContext = ExecutionContext.Implicits.global
  ): Executor[Future] =
    (client: HttpClient, request: FlagrRequest) => {
      val promise = Promise[HttpResponse]()
      val callback: Either[Throwable, HttpResponse] => Unit = {
        case Left(t)  => promise.tryFailure(t)
        case Right(r) => promise.trySuccess(r)
      }
      client.send(request, callback)
      promise.future
    }

  implicit def CatsApplicative[F[_]: Async]: Executor[F] =
    (client: HttpClient, request: FlagrRequest) =>
      Async[F].async_[HttpResponse](k => client.send(request, k))

}
