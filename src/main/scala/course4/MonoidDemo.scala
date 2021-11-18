package course4

object MonoidDemo extends App {

  case class CAD(amount: BigDecimal)

  val a = CAD(12.34)
  val b = CAD(15.49)


  implicit def addable(a: CAD) = new {
    def |+|(b: CAD)(implicit combine: (CAD, CAD) => CAD): CAD = combine(a, b)
  }

  implicit def combine(a: CAD, b: CAD) = CAD(a.amount + b.amount)
  println(a |+| b)

}
