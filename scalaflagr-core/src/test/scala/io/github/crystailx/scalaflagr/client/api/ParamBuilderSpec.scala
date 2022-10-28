package io.github.crystailx.scalaflagr.client.api

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues._

class ParamBuilderSpec extends AnyFlatSpec with Matchers {
  it must "build parameter map from 1 param data" in {
    val builder = new ParamMapBuilder {}
    case class Params1(v1: String = "hello world")
    val params = Params1()
    val built = builder.buildParamsMap(params)
    built.get("v1").value mustBe "hello world"
  }
  it must "build parameter map from 2 param data" in {
    val builder = new ParamMapBuilder {}
    case class Params2(v1: String = "hello world", v0: Int = 456)
    val params = Params2()
    val built = builder.buildParamsMap(params)
    built.get("v1").value mustBe "hello world"
    built.get("v0").value mustBe "456"
  }
  it must "build parameter map from 3 param data" in {
    val builder = new ParamMapBuilder {}
    case class Params2(v1: String = "hello world", v2: Int = 456, v3: Boolean = true)
    val params = Params2()
    val built = builder.buildParamsMap(params)
    built.get("v1").value mustBe "hello world"
    built.get("v2").value mustBe "456"
    built.get("v3").value mustBe "true"
  }

}
