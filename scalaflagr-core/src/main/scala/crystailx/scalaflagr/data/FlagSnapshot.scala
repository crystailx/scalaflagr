package crystailx.scalaflagr.data

case class FlagSnapshot(
  id: Long,
  updatedBy: Option[String] = None,
  flag: Flag,
  updatedAt: String
)
