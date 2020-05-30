import org.junit.Test
import org.junit.Assert._

class ShowTest {
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

  @Test def derivingTest(): Unit = {
    case class Nyan(foo: Int, bar: String)
    val nyan = Nyan(foo = 42, bar = "piyo")
    println(summon[Show[Nyan]].show(nyan))
    assertEquals(
      """Nyan(foo = 42, bar = "piyo")""",
      summon[Show[Nyan]].show(nyan)
    )
  }
}
