package course1

import scala.io
import timetools.DateTimeWrapper._
object TestCourse1 extends App {
  /* 有一堆销售数据， 请统计有多少笔超过了5000？ 最高和最低销售额？ 打印全部人和销售额 */
  case class SaleDetails(saller: String, amount: BigDecimal, time: String)

  val bufferedSource = io.Source.fromFile("C:\\Users\\zhiwi\\code\\couses\\input.csv")
  val lines = bufferedSource.getLines()

  implicit class Tool[A](a: A) {
    def p = println(a)
  }

  val listOfSaleDetails: Array[SaleDetails] = lines.map { line =>
    val cols = line.split(",").map(_.trim)
    // Todo: Validation
    SaleDetails(cols(0), BigDecimal(cols(1)), cols(2))
  }.toArray

  def lambda(in: SaleDetails): Boolean = in.amount > 5000
  val transferred = listOfSaleDetails.filter(_.amount > 5000)

  println(s"Number of deals over 5000 is ${listOfSaleDetails.count(_.amount > 5000)}")

  println(s"Max amount deal is ${listOfSaleDetails.map(_.amount).max}")

  println(listOfSaleDetails.size)

  printf("2004-01-09T17:28:15.618-05:00".dateNumber.toString)
  val maxSaleByDay = listOfSaleDetails.groupBy(_.time.dateNumber).map{
    case(_, arr) => arr.map(_.amount).sum
  }.max
  println(maxSaleByDay)

  def addOne[A](f: A => BigDecimal)(a: A): BigDecimal = {
    f(a) + 1

  }

  val strAddOne = addOne[List[_]](_.size) _

  strAddOne(List("HO")).p


  bufferedSource.close()
}
