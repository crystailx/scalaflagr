package io.github.crystailx.scalaflagr.client

import io.github.crystailx.scalaflagr.client.api.EvaluationApi
import io.github.crystailx.scalaflagr.client.syntax.EvaluationSyntax

trait EvaluationClient[F[_]] extends EvaluationApi[F] with EvaluationSyntax
