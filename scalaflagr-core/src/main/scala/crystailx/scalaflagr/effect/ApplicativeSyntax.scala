package crystailx.scalaflagr.effect

import scala.concurrent.{ ExecutionContext, Future }

trait ApplicativeSyntax {

  implicit def futureApplicative(implicit
    ec: ExecutionContext
  ): Applicative[Future] =
    new FutureApplicative()

}
