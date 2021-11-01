package course1

import scala.io
import timetools.DateTimeWrapper._
object Test extends App {
  /* 有一堆销售数据， 请统计有多少笔超过了5000？ 最高和最低销售额？ 打印全部人和销售额 */
  case class SaleDetails(saller: String, amount: BigDecimal, time: String)

  val bufferedSource = io.Source.fromFile("C:\\temp\\test\\input.csv")
  val lines = bufferedSource.getLines()
  val listOfSaleDetails: Array[SaleDetails] = lines.map { line =>
    val cols = line.split(",").map(_.trim)
    // Todo: Validation
    SaleDetails(cols(0), BigDecimal(cols(1)), cols(2))
  }.toArray


  //def lambda(in: SaleDetails): Boolean = in.amount > 5000
  val transferred = listOfSaleDetails.map(_.amount).filter(_ > 5000)
  println(transferred)

//  println(s"Number of deals over 5000 is ${listOfSaleDetails.count(_.amount > 5000)}")
//  println(s"Max amount deal is ${listOfSaleDetails.map(_.amount).max}")
//  println(listOfSaleDetails.size)
  /*
  //printf("2004-01-09T17:28:15.618-05:00".dateNumber.toString)
  val maxSaleByDay = listOfSaleDetails.groupBy(_.time.dateNumber).map{
    case(_, arr) => arr.map(_.amount).sum
  }.max
  println(maxSaleByDay)*/
  bufferedSource.close()
}
