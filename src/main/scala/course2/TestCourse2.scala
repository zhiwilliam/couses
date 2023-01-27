package course2

import timetools.DateTimeWrapper._
object TestCourse2 extends App {

  case class Sale(id: String, amount: BigDecimal, dateTime: String)

  case class DailySaleRecord(date: DateNum, id: String, dailyAmount: BigDecimal)

  val salesList = List(Sale("1", 0.1, "20010101"),
    Sale("1", 0.1, "20010102"), Sale("2", 0.5, "20010102"), Sale("2", 0.6, "20010102"),
    Sale("1", 1.0, "20010103"), Sale("1", 0.5, "20010103"), Sale("2", 0.6, "20010103"),
    Sale("1", 0.1, "20010104"), Sale("1", 0.5, "20010104"), Sale("2", 0.6, "20010104"),
    Sale("1", 1.0, "20010105"), Sale("2", 0.8, "20010105"), Sale("2", 0.9, "20010105")
  )
  val dailyWinners = salesList.groupBy(_.dateTime.dateNumber).view.mapValues(_.groupBy(_.id).view.mapValues(_.map(_.amount).sum).maxBy(_._2)).toMap
  val dailyWinnersRecords = dailyWinners.map { case (date, (id, dailyAmount)) => DailySaleRecord(date, id, dailyAmount) }.toVector

  import GenericTools.implicits._
  val historicalOrderedRecords = dailyWinnersRecords.listBestRecordsOrderBy(_.date) onRecordValue (x => x.dailyAmount) // DSL

  println(historicalOrderedRecords)
}
