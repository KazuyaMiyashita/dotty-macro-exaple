import org.junit.Test
import org.junit.Assert._

class ShowTest {
  @Test def intTest(): Unit = {
    assertEquals(
      "42",
      summon[Show[Int]].show(42)
    )
  }

  @Test def longTest(): Unit = {
    assertEquals(
      "12345678910L",
      summon[Show[Long]].show(12345678910L)
    )
  }

  @Test def stringTest(): Unit = {
    assertEquals(
      "\"aiueo\\n12345\"", // "aiueo\n12345"
      summon[Show[String]].show("aiueo\n12345")
    )
  }

  @Test def instantTest1(): Unit = {
    import java.time.Instant
    assertEquals(
      """Instant.parse("2007-12-03T10:15:30.123Z")""", // ".000" is lost
      summon[Show[Instant]].show(Instant.parse("2007-12-03T10:15:30.123Z"))
    )
  }

  @Test def instantTest2(): Unit = {
    import java.time.Instant
    assertEquals(
      """Instant.parse("2007-12-03T10:15:30Z")""", // ".000" is lost
      summon[Show[Instant]].show(Instant.parse("2007-12-03T10:15:30.000Z"))
    )
  }

  @Test def instantTest3(): Unit = {
    import java.time.Instant
    assertEquals(
      """Instant.parse("2007-12-03T10:15:30.123456Z")""",
      summon[Show[Instant]].show(Instant.parse("2007-12-03T10:15:30.123456Z"))
    )
  }

  @Test def optionTest1(): Unit = {
    val value = Some("a")
    assertEquals(
      """Some("a")""",
      summon[Show[Option[String]]].show(value)
    )
  }

  @Test def optionTest2(): Unit = {
    val value = None
    assertEquals(
      """None""",
      summon[Show[Option[String]]].show(value)
    )
  }

  @Test def listTest1(): Unit = {
    val value = List(1, 2, 3)
    assertEquals(
      """List(1, 2, 3)""",
      summon[Show[List[Int]]].show(value)
    )
  }

  @Test def listTest2(): Unit = {
    val value = List()
    assertEquals(
      """List()""",
      summon[Show[List[String]]].show(value)
    )
  }

  @Test def buildStrTest(): Unit = {
    // ex. Nyan(foo = 42, bar = "piyo")
    val tpe = "Nyan"
    val labels = List("foo", "bar")
    val elems = List("42", "\"piyo\"")
    assertEquals(
      """Nyan(foo = 42, bar = "piyo")""",
      Show.buildStr(tpe, labels, elems)
    )
  }

  @Test def derivingTest1(): Unit = {
    case class Nyan(foo: Int, bar: String)
    val nyan = Nyan(foo = 42, bar = "piyo")
    assertEquals(
      """Nyan(foo = 42, bar = "piyo")""",
      summon[Show[Nyan]].show(nyan)
    )
  }

  @Test def derivingTest2(): Unit = {
    case class Foo(value: Int)
    case class Bar(value: String)
    case class Mofu(foo: Foo, bar: Bar)
    val mofu = Mofu(foo = Foo(value = 42), bar = Bar(value = "piyo"))
    println(summon[Show[Mofu]].show(mofu))
    assertEquals(
      """Mofu(foo = Foo(value = 42), bar = Bar(value = "piyo"))""",
      summon[Show[Mofu]].show(mofu)
    )
  }

}
