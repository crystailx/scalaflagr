package com.crystalxyen.scalaflagr.data

case class EvalContext(
    // entityID is used to deterministically at random to evaluate the flag result. If it's empty, flagr will randomly generate one.
    entityID: Option[String] = None,
    entityType: Option[String] = None,
    entityContext: Option[io.circe.Json] = None,
    enableDebug: Option[Boolean] = None,
    // flagID
    flagID: Option[Long] = None,
    // flagKey. flagID or flagKey will resolve to the same flag. Either works.
    flagKey: Option[String] = None,
    // flagTags. flagTags looks up flags by tag. Either works.
    flagTags: Option[List[String]] = None,
    // determine how flagTags is used to filter flags to be evaluated. OR extends the evaluation to those which contains at least one of the provided flagTags or AND limit the evaluation to those which contains all the flagTags.
    flagTagsOperator: Option[String] = None
)
