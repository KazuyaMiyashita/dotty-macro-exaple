import java.time.Instant

trait Show[A] {
  def show(value: A): String
}

object Show {

  given as Show[Int] {
    override def show(value: Int): String = value.toString
  }

  given as Show[Long] {
    override def show(value: Long): String = s"${value.toString}L"
  }

  given as Show[String] {
    override def show(value: String): String = "\"" + value + "\""
    // todo excape sequense
  }

  given as Show[Instant] {
    override def show(value: Instant): String = {
      s"""Instant.parse("${value.toString}")"""
    }
  }

  import scala.deriving.{Mirror, productElement}
  import scala.compiletime.{constValue, erasedValue, error, summonFrom, summonInline}

  inline def showElem[T](value: T): String = summonInline[Show[T]].show(value)
  inline def showElems[Elems <: Tuple](value: Any, idx: Int): List[String] =
    inline erasedValue[Elems] match {
      case _: (t *: ts) => showElem[t](productElement[t](value, idx)) :: showElems[ts](value, idx + 1)
      case _ => Nil
    }

  inline given derived[T](using ev: Mirror.Of[T]) as Show[T] = new Show[T] {
    override def show(value: T): String = {
      inline ev match {
        case m: Mirror.ProductOf[T] => {
          val tpe: String = value.asInstanceOf[Product].productPrefix
          val labels: List[String] = value.asInstanceOf[Product].productElementNames.toList
          val elems: List[String] = showElems[m.MirroredElemTypes](value, 0)
          buildStr(tpe, labels, elems)
        }
        case _ => error("derived Show only supports case classes")
      }
    }

  }

  def buildStr(tpe: String, labels: List[String], elems: List[String]): String = {
    val fields: String = (labels zip elems).map((l, e) => l + " = " + e).mkString(", ")
    s"${tpe}(${fields})"
  }

}