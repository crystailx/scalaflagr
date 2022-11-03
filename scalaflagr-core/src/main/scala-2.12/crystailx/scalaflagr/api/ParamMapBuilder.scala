package crystailx.scalaflagr.api

import shapeless.ops.hlist.ToTraversable
import shapeless.{ HList, LabelledGeneric }
import shapeless.ops.record.Keys

trait ParamMapBuilder {

  private[api] def buildParamsMap[T <: Product, L <: HList, R <: HList](params: T)(implicit
    gen: LabelledGeneric.Aux[T, L],
    keys: Keys.Aux[L, R],
    ts: ToTraversable.Aux[R, List, Symbol]
  ): Map[String, String] = {
    val fieldNames = keys().toList.map(_.name)
    val values = params.productIterator.toList.map(_.toString)
    Map(fieldNames zip values: _*)
  }

}
