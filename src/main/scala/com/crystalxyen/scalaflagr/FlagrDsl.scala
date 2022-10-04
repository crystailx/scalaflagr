package com.crystalxyen.scalaflagr

import com.crystalxyen.scalaflagr.api.EvaluationApi
import com.crystalxyen.scalaflagr.handlers.EvaluationHandlers

object FlagrDsl extends EvaluationApi with EvaluationHandlers {}
