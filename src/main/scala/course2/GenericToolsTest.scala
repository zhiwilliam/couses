package course2

object GenericToolsTest extends App {
  case class Sale(id: String, amount: BigDecimal, dateTime: String)

  val bufferedSource = io.Source.fromFile("C:\\temp\\test\\input.csv")
  val salesRecords = bufferedSource.getLines.map { line =>
    val cols = line.split(",").map(_.trim)
    Sale(cols(0), BigDecimal(cols(1)), cols(2))
  }

  import timetools.DateTimeWrapper._

  val salesList = salesRecords.toList

  case class SaleRecord(date: Int, id: String, total: BigDecimal)
  val dayWinners: Array[SaleRecord] = salesList.groupBy(_.dateTime.dateNumber).view.mapValues(salesList =>
    salesList.groupBy(_.id).view.mapValues(_.map(_.amount).sum).maxBy(_._2)
  ).map{case (date, (id, amount))=>SaleRecord(date, id, amount)}.toArray

  val winners = dayWinners.sortBy(_.date).foldLeft((BigDecimal(0), List[SaleRecord]())) {
    case ((rec, winners), next) =>
      if (next.total > rec) {
        (next.total, next :: winners)
      }
      else (rec, winners)
  }
  //dayWinners.foreach(println)
  winners._2.foreach(println)

  import GenericTools.implicits._

  val myWinners = dayWinners.listBestRecords(_.total) of (_.date)
  //val myWinners = List[SaleRecord]().listAllHistoricalRecords(_.date, _.sum)
  myWinners.foreach(println)
  bufferedSource.close
}
