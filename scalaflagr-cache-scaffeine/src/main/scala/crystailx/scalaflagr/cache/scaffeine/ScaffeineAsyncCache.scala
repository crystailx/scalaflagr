package crystailx.scalaflagr.cache.scaffeine

import com.github.blemale.scaffeine.AsyncCache
import crystailx.scalaflagr.cache.Cacher
import crystailx.scalaflagr.data.EvalResult

import scala.concurrent.{ ExecutionContext, Future }

case class ScaffeineAsyncCache[K](
  underlying: AsyncCache[K, EvalResult]
)(implicit ec: ExecutionContext)
    extends Cacher[K, Future] {

  override def set(key: K, evalResult: EvalResult): Future[Unit] =
    Future(underlying.put(key, Future(evalResult)))

  override def get(key: K): Future[Option[EvalResult]] =
    Future
      .sequence(Option.option2Iterable(underlying.getIfPresent(key)))
      .map(_.headOption)

  override def invalidate(key: K): Future[Unit] = Future(underlying.synchronous().invalidate(key))

  override def invalidateAll(): Future[Unit] = Future(underlying.synchronous().invalidateAll())
}
