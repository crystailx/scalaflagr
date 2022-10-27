package io.github.crystailx.scalaflagr.client

import com.softwaremill.sttp._
import io.github.crystailx.scalaflagr.effect.Functor

import scala.concurrent.{ ExecutionContext, Future }

class SttpManagerClient(override protected val config: FlagrConfig)(implicit
  override protected val functor: Functor[Future],
  override protected val sttpBackend: SttpBackend[Future, Nothing],
  override protected val ec: ExecutionContext
) extends SttpHttpClient
    with ManagerClient[Future]
