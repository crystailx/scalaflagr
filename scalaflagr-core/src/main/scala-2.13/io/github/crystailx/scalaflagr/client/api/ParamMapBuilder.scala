package io.github.crystailx.scalaflagr.client.api

trait ParamMapBuilder {

  private[api] def buildParamsMap[T <: Product](params: T): Map[String, String] =
    params.productElementNames
      .zip(params.productIterator)
      .filterNot(_._2 == None)
      .map { kv =>
        kv._1 -> (kv._2 match {
          case value: Option[_] =>
            value.fold("")(_.toString)
          case value => value.toString
        })
      }
      .toMap

}
