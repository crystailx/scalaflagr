package crystailx.scalaflagr.cache.redis

import crystailx.scalaflagr.cache.Cacher
import crystailx.scalaflagr.data.EvalResult
import scredis.commands.{ KeyCommands, StringCommands }
import scredis.serialization.{ Reader, Writer }

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ ExecutionContext, Future }

class RedisCache(
  redisServer: StringCommands with KeyCommands,
  ttlOpt: Option[FiniteDuration] = None
)(implicit
  ex: ExecutionContext,
  valueWriter: Writer[EvalResult],
  valueReader: Reader[EvalResult]
) extends Cacher[String, Future] {

  override def set(key: String, evalResult: EvalResult): Future[Unit] =
    redisServer.set(key, evalResult, ttlOpt).map(_ => ())

  override def get(key: String): Future[Option[EvalResult]] = redisServer.get(key)

  override def invalidate(key: String): Future[Unit] =
    redisServer.del(key).map(_ => ())

  override def invalidateAll(): Future[Unit] = Future.failed(new Exception("Unsupported operation"))
}
