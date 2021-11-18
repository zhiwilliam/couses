package course4

object AutoMonoid extends App {
  import cats.implicits._
  case class CAD(amount: BigDecimal) extends AnyVal
  case class CatalogItem(id: String) extends AnyVal
  case class ShoppingCart(items: List[CatalogItem], totalPrice: CAD)

  import cats.derived.auto.semigroup._
  import cats.derived.MkCommutativeMonoid.mkCommutativeMonoidGeneric

  // That's it! Now we can rely on automatically derived type classes:

  println(CAD(32.38) |+| CAD(56.94))

  val res = ShoppingCart(List(CatalogItem("Shirt")), CAD(20)) |+| ShoppingCart(List(CatalogItem("Shoes")), CAD(50))
  println(res)
}
