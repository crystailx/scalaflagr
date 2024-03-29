package crystailx.scalaflagr.data

import crystailx.scalaflagr.json.Encoder

sealed trait FlagrContext {
  val flagKey: String

  val entityID: String

  val entityType: String

  protected[scalaflagr] val entityContext: Option[RawValue]

  def toEvalContext: EvalContext =
    EvalContext(
      Some(entityID),
      Some(entityType),
      entityContext,
      flagKey = Some(flagKey)
    )
}

case class BasicContext(
  flagKey: String,
  override val entityID: String = "",
  override val entityType: String = ""
) extends FlagrContext {
  override protected[scalaflagr] val entityContext: Option[RawValue] = None
}

case class EntityContext(
  flagKey: String,
  override protected[scalaflagr] val entityContext: Option[RawValue],
  override val entityID: String,
  override val entityType: String
) extends FlagrContext {
  def flagKey(value: String): EntityContext = copy(flagKey = value)
  def entityID(value: String): EntityContext = copy(entityID = value)
  def entityType(value: String): EntityContext = copy(entityType = value)

  def entityContext[T](value: T)(implicit encoder: Encoder[T]): EntityContext =
    copy(entityContext = Option(encoder.encode(value)))
}

object EntityContext {

  def apply[T](
    flagKey: String,
    entityContext: T,
    entityID: String = "",
    entityType: String = ""
  )(implicit encoder: Encoder[T]): EntityContext =
    new EntityContext(flagKey, Option(encoder.encode(entityContext)), entityID, entityType)

}
