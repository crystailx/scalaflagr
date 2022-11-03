package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestHandler
import crystailx.scalaflagr.data.Health

import scala.language.implicitConversions

trait HealthApi {

  private val apiBasePath: String = "/health"
  import RequestHandler.get

  def healthStatus: RequestHandler[Nothing, Health] =
    get(apiBasePath)
}
