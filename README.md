## dotty-macro-example

### Show[A]

```scala
scala> case class Nyan(foo: Int, bar: String)
// defined case class Nyan

scala> val nyan = Nyan(42, "piyo")
val nyan: Nyan = Nyan(42,piyo)

scala> summon[Show[Nyan]].show(nyan)
val res0: String = Nyan(foo = 42, bar = "piyo")

```

## sbt project compiled with Dotty

### Usage

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`, `sbt console` will start a Dotty REPL.

For more information on the sbt-dotty plugin, see the
[dotty-example-project](https://github.com/lampepfl/dotty-example-project/blob/master/README.md).
