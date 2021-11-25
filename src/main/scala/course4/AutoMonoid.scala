package course4

object AutoMonoid extends App {
  import cats.implicits._
  /*case class CAD(amount: BigDecimal) extends AnyVal
  case class CatalogItem(id: String) extends AnyVal
  case class ShoppingCart(items: List[CatalogItem], totalPrice: CAD)*/

  import cats.derived.auto.semigroup._
  import cats.derived.MkCommutativeMonoid.mkCommutativeMonoidGeneric

  // That's it! Now we can rely on automatically derived type classes:

  //println(CAD(32.38) |+| CAD(56.94))

  //val res = ShoppingCart(List(CatalogItem("Shirt"), CatalogItem("Table")), CAD(223.04)) |+| ShoppingCart(List(CatalogItem("Shoes")), CAD(50))
  //println(res)

/*  case class Department(name: String)
  val data1 = Map(Department("cloths") -> Map(
                CatalogItem("Shirt") -> CAD(10.0),
                CatalogItem("Shoes") -> CAD(12.7),
              ),
                Department("Furniture") -> Map(
                CatalogItem("Table") -> CAD(273.27),
                CatalogItem("Tent") -> CAD(200.32))
  )

  val data2 = Map ( Department("Furniture") -> Map(CatalogItem("Table") -> CAD(280.0)),
                    Department("Cloths") -> Map(CatalogItem("Shoes") -> CAD(23.54),
                                                CatalogItem("Shirt") -> CAD(18.75))
                  )*/

  def getValue(id: Int): Option[String] = Option("2")

  println(getValue(1).flatMap(x => getValue(x.toInt)).flatMap(x=> getValue(x.toInt)))

  val a = for {
    a1 <- getValue(1)
    a2 <- getValue(a1.toInt)
    a3 <- getValue(a2.toInt)
  } yield a3
  println(a)
  //println(data1 |+| data2)
}
