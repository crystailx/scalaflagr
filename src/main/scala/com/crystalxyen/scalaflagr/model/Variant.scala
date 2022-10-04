/**
  * Flagr
  * Flagr is a feature flagging, A/B testing and dynamic configuration microservice. The base path for all the APIs is \"/api/v1\".
  *
  * OpenAPI spec version: 1.1.13
  *
  *
  * NOTE: This class is auto generated by the swagger code generator program.
  * https://github.com/swagger-api/swagger-codegen.git
  * Do not edit the class manually.
  */
package com.crystalxyen.scalaflagr.model

case class Variant(
    id: Option[Long] = None,
    key: String,
    attachment: Option[Any] = None
)
