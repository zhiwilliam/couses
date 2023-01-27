package course3

object TestCourse3 extends App {
  val value = Map("a" -> List(1, 2), "b" -> List(2, 3, 4))
  import MapTools.implicits._
  import TraversableGeneric.implicits._
  println(value.filterByValues(_ > 2))
  println(value.partitionByValues(_ > 2))
}
