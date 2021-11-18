package course4

object MonoidNormal extends App {
  import cats._
  import cats.implicits._

  case class CAD(amount: BigDecimal) extends AnyVal

  implicit val CADMonoid: Monoid[CAD] = new Monoid[CAD] {
    def combine(a1: CAD, a2: CAD): CAD =
      CAD(a1.amount + a2.amount)
    def empty: CAD = CAD(0)
  }

  val res = CAD(23.4) |+| CAD(12.44)
  println(res)
}
