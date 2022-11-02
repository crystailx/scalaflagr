package crystailx.scalaflagr.api

import crystailx.scalaflagr.RequestBuilder

import scala.language.implicitConversions

trait HealthApi {

  private val apiBasePath: String = "/health"
  import RequestBuilder.get

  def healthStatus: RequestBuilder[Nothing] =
    get(apiBasePath)
}
