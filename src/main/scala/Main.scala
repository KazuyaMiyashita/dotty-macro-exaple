import java.time.Instant

object Main {

  def main(args: Array[String]): Unit = {

    val intShow = summon[Show[Int]]
    println(intShow.show(42))

    println(summon[Show[Instant]].show(Instant.now()))

  }

}
