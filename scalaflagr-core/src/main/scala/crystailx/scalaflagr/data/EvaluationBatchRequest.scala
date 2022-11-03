package crystailx.scalaflagr.data

case class EvaluationBatchRequest(
  entities: List[EvaluationEntity],
  enableDebug: Option[Boolean] = None,
  // flagIDs
  flagIDs: Option[List[Long]] = None,
  // flagKeys. Either flagIDs, flagKeys or flagTags works. If pass in multiples, Flagr may return duplicate results.
  flagKeys: Option[List[String]] = None,
  // flagTags. Either flagIDs, flagKeys or flagTags works. If pass in multiples, Flagr may return duplicate results.
  flagTags: Option[List[String]] = None,
  // determine how flagTags is used to filter flags to be evaluated. OR extends the evaluation to those which contains at least one of the provided flagTags or AND limit the evaluation to those which contains all the flagTags.
  flagTagsOperator: Option[String] = None
) {
  def entities(value: List[EvaluationEntity]): EvaluationBatchRequest = copy(entities = value)

  def addEntity(value: EvaluationEntity): EvaluationBatchRequest =
    copy(entities = entities :+ value)
  def enableDebug(value: Boolean): EvaluationBatchRequest = copy(enableDebug = Option(value))
  def flagIDs(value: List[Long]): EvaluationBatchRequest = copy(flagIDs = Option(value))
  def flagKeys(value: List[String]): EvaluationBatchRequest = copy(flagKeys = Option(value))
  def flagTags(value: List[String]): EvaluationBatchRequest = copy(flagTags = Option(value))

  def flagTagsOperator(value: String): EvaluationBatchRequest =
    copy(flagTagsOperator = Option(value))
}
