package io.github.crystailx.scalaflagr.data

import io.github.crystailx.scalaflagr.json.Encoder

case class EvalContext(
                        // entityID is used to deterministically at random to evaluate the flag result. If it's empty, flagr will randomly generate one.
                        entityID: Option[String] = None,
                        entityType: Option[String] = None,
                        protected val entityContext: Option[RawValue] = None,
                        enableDebug: Option[Boolean] = None,
                        // flagID
                        flagID: Option[Long] = None,
                        // flagKey. flagID or flagKey will resolve to the same flag. Either works.
                        flagKey: Option[String] = None,
                        // flagTags. flagTags looks up flags by tag. Either works.
                        flagTags: Option[List[String]] = None,
                        // determine how flagTags is used to filter flags to be evaluated. OR extends the evaluation to those which contains at least one of the provided flagTags or AND limit the evaluation to those which contains all the flagTags.
                        flagTagsOperator: Option[String] = None
) {
  def entityID(value: String): EvalContext = copy(entityID = Option(value))

  def entityType(value: String): EvalContext = copy(entityType = Option(value))

  def entityContext(value: RawValue): EvalContext = copy(entityContext = Option(value))
  def entityContext(value: String): EvalContext = copy(entityContext = Option(value.getBytes))

  def entityContext[T](value: T)(implicit encoder: Encoder[T]): EvalContext =
    copy(entityContext = Option(encoder.encode(value)))
  def enableDebug(value: Boolean): EvalContext = copy(enableDebug = Option(value))
  def flagID(value: Long): EvalContext = copy(flagID = Option(value))
  def flagKey(value: String): EvalContext = copy(flagKey = Option(value))
  def flagTags(value: List[String]): EvalContext = copy(flagTags = Option(value))

  def addFlagTag(value: String): EvalContext =
    copy(flagTags = flagTags.map(_ :+ value).orElse(Option(List(value))))
  def flagTagsOperator(value: String): EvalContext = copy(flagTagsOperator = Option(value))
}
