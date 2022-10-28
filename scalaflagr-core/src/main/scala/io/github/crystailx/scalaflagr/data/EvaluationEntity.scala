package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Encoder

case class EvaluationEntity(
  entityID: Option[String] = None,
  entityType: Option[String] = None,
  protected val entityContext: Option[RawValue] = None
) {
  def entityID(value: String): EvaluationEntity = copy(entityID = Option(value))
  def entityType(value: String): EvaluationEntity = copy(entityType = Option(value))
  def entityContext(value: RawValue): EvaluationEntity = copy(entityContext = Option(value))

  def entityContext(value: String): EvaluationEntity =
    copy(entityContext = Option(value.getBytes()))

  def entityContext[T](value: T)(implicit encoder: Encoder[T]): EvaluationEntity =
    copy(entityContext = Option(encoder.encode(value)))
}
