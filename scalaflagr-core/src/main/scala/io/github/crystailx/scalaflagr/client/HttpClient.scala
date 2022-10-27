package io.github.crystailx.scalaflagr.client

import io.github.crystailx.scalaflagr.client.HttpMethod._

trait HttpClient[F[_]] {

  protected def get(url: String, params: Map[String, String] = Map.empty): F[String] =
    send(Get, url, None, params)

  protected def delete(url: String, params: Map[String, String] = Map.empty): F[String] =
    send(Delete, url, None, params)

  protected def post(url: String): F[String] =
    send(Post, url, None, Map.empty)

  protected def post(url: String, params: Map[String, String]): F[String] =
    send(Post, url, None, params)

  protected def post(
    url: String,
    body: String
  ): F[String] = send(Post, url, Option(body), Map.empty)

  protected def post(
    url: String,
    body: String,
    params: Map[String, String]
  ): F[String] = send(Post, url, Option(body), params)

  protected def put(url: String): F[String] =
    send(Put, url, None, Map.empty)

  protected def put(url: String, params: Map[String, String]): F[String] =
    send(Put, url, None, params)

  protected def put(url: String, body: String): F[String] =
    send(Put, url, Option(body), Map.empty)

  protected def put(url: String, body: String, params: Map[String, String]): F[String] =
    send(Put, url, Option(body), params)

  protected def patch(url: String): F[String] =
    send(Patch, url, None, Map.empty)

  protected def patch(url: String, params: Map[String, String]): F[String] =
    send(Patch, url, None, params)

  protected def patch(
    url: String,
    body: String
  ): F[String] = send(Patch, url, Option(body), Map.empty)

  protected def patch(
    url: String,
    body: String,
    params: Map[String, String] = Map.empty
  ): F[String] = send(Patch, url, Option(body), params)

  protected def send(
    method: HttpMethod,
    url: String,
    body: Option[String],
    params: Map[String, String]
  ): F[String]
}
