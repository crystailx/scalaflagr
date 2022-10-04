package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.client.HttpResponse
import io.circe.parser

object FlagrErrorParser {

  def parse(resp: HttpResponse): FlagrError = {
    def defaultError: FlagrError = FlagrError(
      resp.statusCode.toString,
      resp.statusCode.toString,
      None,
      None,
      None,
      Nil,
      None
    )

    resp.entity match {
      case Some(entity) =>
        parser
          .parse(entity.content)
          .fold(
            _ => defaultError,
            node =>
              node.hcursor.get[FlagrError]("error").getOrElse(defaultError)
          )
      case _ => defaultError
    }
  }
}
