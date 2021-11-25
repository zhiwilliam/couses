package course4

object MonoidDemo extends App {

  case class CAD(amount: BigDecimal)

  val a = CAD(12.34)
  val b = CAD(15.49)


  trait Addable[A]{
    def add(a: A, b: A): A
    //def |+|(b: A)(implicit combine: (A, A) => A): A
  }

  implicit object CadIsAddable extends Addable[CAD]{
    //def |+|(b: CAD)(implicit combine: (CAD, CAD) => CAD): CAD = combine(a, b)

    override def add(a: CAD, b: CAD): CAD = combine(a, b)
  }

  implicit def combine(a: CAD, b: CAD) = CAD(a.amount + b.amount)

  def tripleAdd[A: Addable](a: A, b: A, c: A): A = {
    val addable = implicitly[Addable[A]]
    //context bound
    addable.add(a, addable.add(b, c))
  }
  println(tripleAdd(CAD(1), CAD(2), CAD(3)))


}
