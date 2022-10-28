package io.github.crystailx.scalaflagr.client

import io.github.crystailx.scalaflagr.client.HttpMethod._
import io.github.crystailx.scalaflagr.data.RawValue

trait HttpClient[F[_]] {

  protected def get(url: String, params: Map[String, String] = Map.empty): F[RawValue] =
    send(Get, url, None, params)

  protected def delete(url: String, params: Map[String, String] = Map.empty): F[RawValue] =
    send(Delete, url, None, params)

  protected def post(url: String): F[RawValue] =
    send(Post, url, None, Map.empty)

  protected def post(url: String, params: Map[String, String]): F[RawValue] =
    send(Post, url, None, params)

  protected def post(url: String, body: RawValue): F[RawValue] =
    send(Post, url, Option(body), Map.empty)

  protected def post(url: String, body: RawValue, params: Map[String, String]): F[RawValue] =
    send(Post, url, Option(body), params)

  protected def put(url: String): F[RawValue] =
    send(Put, url, None, Map.empty)

  protected def put(url: String, params: Map[String, String]): F[RawValue] =
    send(Put, url, None, params)

  protected def put(url: String, body: RawValue): F[RawValue] =
    send(Put, url, Option(body), Map.empty)

  protected def put(url: String, body: RawValue, params: Map[String, String]): F[RawValue] =
    send(Put, url, Option(body), params)

  protected def patch(url: String): F[RawValue] =
    send(Patch, url, None, Map.empty)

  protected def patch(url: String, params: Map[String, String]): F[RawValue] =
    send(Patch, url, None, params)

  protected def patch(url: String, body: RawValue): F[RawValue] =
    send(Patch, url, Option(body), Map.empty)

  protected def patch(
    url: String,
    body: RawValue,
    params: Map[String, String] = Map.empty
  ): F[RawValue] = send(Patch, url, Option(body), params)

  protected def send(
    method: HttpMethod,
    url: String,
    body: Option[RawValue],
    params: Map[String, String]
  ): F[RawValue]
}
