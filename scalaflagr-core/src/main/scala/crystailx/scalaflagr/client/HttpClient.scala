package crystailx.scalaflagr.client

import crystailx.scalaflagr.data.RawValue
import crystailx.scalaflagr.{ FlagrConfig, FlagrRequest }

trait HttpClient[F[_]] {
  protected val config: FlagrConfig
  def send(request: FlagrRequest): F[RawValue]

}
