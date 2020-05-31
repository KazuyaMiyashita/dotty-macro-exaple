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
    override def show(value: String): String = {
      val escaped = value.flatMap {
        case '\\' => "\\\\"
        case '\b' => "\\b"
        case '\f' => "\\f"
        case '\n' => "\\n"
        case '\r' => "\\r"
        case '\t' => "\\t"
        case '\'' => "\\\'"
        case '\"' => "\\\""
        case other => other.toString
      }
      "\"" + escaped + "\""
    }
  }

  given as Show[Instant] {
    override def show(value: Instant): String = {
      s"""Instant.parse("${value.toString}")"""
    }
  }

  given showOption[A](using ev: Show[A]) as Show[Option[A]] {
    override def show(value: Option[A]): String = {
      value match {
        case Some(v) => s"Some(${ev.show(v)})"
        case None => "None"
      }
    }
  }

  given showList[A](using ev: Show[A]) as Show[List[A]] {
    override def show(value: List[A]): String = {
      "List(" + value.map(ev.show).mkString(", ") + ")"
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

  inline def showLabels[Labels <: Tuple]: List[String] = {
    inline erasedValue[Labels] match {
      case _: (l *: ls) => constValue[l].toString :: showLabels[ls]
      case _: Unit => Nil
    }
  }

  inline given derived[T](using ev: Mirror.Of[T]) as Show[T] = new Show[T] {
    override def show(value: T): String = {
      inline ev match {
        case m: Mirror.ProductOf[T] => {
          val tpe: String = constValue[m.MirroredLabel]
          val labels: List[String] = showLabels[m.MirroredElemLabels]
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
