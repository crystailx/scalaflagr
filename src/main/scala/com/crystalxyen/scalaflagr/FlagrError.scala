package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.client.HttpResponse

import scala.collection.mutable
import scala.util.Try

case class FlagrError(
    `type`: String,
    reason: String,
    indexUuid: Option[String],
    index: Option[String],
    shard: Option[String],
    rootCause: Seq[FlagrError],
    causedBy: Option[FlagrError.CausedBy],
    phase: Option[String] = None,
    grouped: Option[Boolean] = None,
    failedShards: Seq[FailedShard] = Seq()
) {

  def asException: Exception =
    causedBy.fold(new RuntimeException(s"${`type`} $reason"))(
      cause =>
        new RuntimeException(
          s"${`type`} $reason",
          new RuntimeException(cause.toString)
        )
    )
}

case class FailedShard(
    shard: Int,
    index: Option[String],
    node: Option[String],
    reason: Option[
      FlagrError
    ] // reason is a nested FlagrError here, rather than a string as it is in FlagrError
)

object FlagrError {

  class CausedBy(
      val `type`: String,
      val reason: String,
      val scriptStack: Seq[String],
      val causedBy: Option[FlagrError.CausedBy]
  ) {
    private val _other = mutable.HashMap[String, String]()

    //noinspection ScalaUnusedSymbol
    private def setOther(k: String, v: String): Unit =
      _other.put(k, v)

    def other(key: String): Option[String] = _other.get(key)

    override def toString: String = s"CausedBy(${`type`},$reason,${_other})"
  }

  def fromThrowable(t: Throwable) =
    FlagrError(
      t.getClass.getCanonicalName,
      t.getLocalizedMessage,
      None,
      None,
      None,
      Nil,
      None
    )

  def parse(r: HttpResponse): FlagrError = FlagrErrorParser.parse(r)
}
