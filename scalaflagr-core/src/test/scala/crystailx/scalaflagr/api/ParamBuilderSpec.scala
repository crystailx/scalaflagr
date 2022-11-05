package crystailx.scalaflagr.api

import org.scalatest.OptionValues._
import org.scalatest.Outcome
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.must.Matchers

class ParamBuilderSpec extends FixtureAnyFlatSpec with Matchers {

  override type FixtureParam = ParamMapBuilder

  override protected def withFixture(test: OneArgTest): Outcome =
    test(new ParamMapBuilder {})

  it must "build parameter map from 1 param data" in { builder =>
    case class Params1(v1: String = "hello world")
    val params = Params1()
    val built = builder.buildParamsMap(params)
    built must have size 1
    built.get("v1").value mustBe "hello world"
  }

  it must "build parameter map from 2 param data" in { builder =>
    case class Params2(v1: String = "hello world", v0: Int = 456)
    val params = Params2()
    val built = builder.buildParamsMap(params)
    built must have size 2
    built.get("v1").value mustBe "hello world"
    built.get("v0").value mustBe "456"
  }

  it must "build parameter map from 3 param data" in { builder =>
    case class Params2(v1: String = "hello world", v2: Int = 456, v3: Boolean = true)
    val params = Params2()
    val built = builder.buildParamsMap(params)
    built must have size 3
    built.get("v1").value mustBe "hello world"
    built.get("v2").value mustBe "456"
    built.get("v3").value mustBe "true"
  }

  it must "not build parameters with None value" in { builder =>
    case class ParamsOption(v1: Option[String], v2: Option[Int], v3: Option[Boolean])
    val params = ParamsOption(Some("hello world"), None, Some(false))
    val built = builder.buildParamsMap(params)
    built must have size 2
    built.get("v1").value mustBe "hello world"
    built.get("v3").value mustBe "false"
  }
}
