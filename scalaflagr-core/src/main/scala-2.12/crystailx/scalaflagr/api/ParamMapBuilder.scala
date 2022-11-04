package crystailx.scalaflagr.api

import shapeless.ops.hlist.ToTraversable
import shapeless.ops.record.Keys
import shapeless.{ HList, LabelledGeneric }

trait ParamMapBuilder {

  private[api] def buildParamsMap[T <: Product, L <: HList, R <: HList](params: T)(implicit
    gen: LabelledGeneric.Aux[T, L],
    keys: Keys.Aux[L, R],
    ts: ToTraversable.Aux[R, List, Symbol]
  ): Map[String, String] = {
    val fieldNames = keys().toList.map(_.name)
    val values = params.productIterator.toList
    val zipped = fieldNames zip values filterNot (_._2 == None) map {
      case (str, value: Option[_]) =>
        str -> value.fold("")(_.toString)
      case (str, value) => str -> value.toString
    }
    Map(zipped: _*)
  }

}
