package course101


// apply unapply: google implement
case class Duck ( weight: Float, color: String)

trait DuckFly {
  def fly: Unit = println("fly")
}

trait DuckWalk {
  def walk: Unit = println("walk")
}
object Test extends App {
  implicit class Tool[A](a: A) {
    def p = println(a)
  }

  // Monad
  // Applicative
  val a = 3
  val b = List(a, 3, 4)
  // functor
  // inline
  val c = b.view.map(x => x - 1).map(_ * 4).map(-_)
  // monad
  val d = b.flatMap(x => Range.inclusive(0, x).toList)

  val z = Right("error")

  val dd = z.flatMap(x => Right(x + "sdfds")).toOption


  c.p

}
