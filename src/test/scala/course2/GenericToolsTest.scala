package course2

import org.scalatest.wordspec.AnyWordSpec

class GenericToolsTest extends AnyWordSpec {
  "listBestHistoricalRecords tool" should {
    import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
    import timetools.DateTimeWrapper._
    case class Sale(id: String, amount: BigDecimal, dateTime: String)
    case class DailySaleRecord(date: DateNum, id: String, dailyAmount: BigDecimal)

    "handle empty data properly" in {
      val dailyWinners = Vector.empty[Sale].groupBy(_.dateTime.dateNumber).view.mapValues(_.groupBy(_.id).view.mapValues(_.map(_.amount).sum).maxBy(_._2)).toMap
      val dailyWinnersRecords = dailyWinners.map{case(date, (id, dailyAmount)) => DailySaleRecord(date, id, dailyAmount)}.toVector
      val myWinners = GenericTools.listBestHistoricalRecords[DailySaleRecord, DateNum, BigDecimal](dailyWinnersRecords, _.date, _.dailyAmount)
      myWinners.isEmpty shouldBe true
    }

    "list all historical records" in {
      val salesList = List(Sale("1", 1.0, "20010101"),
        Sale("1", 1.0, "20010102"), Sale("2", 0.5, "20010102"), Sale("2", 0.6, "20010102"),
        Sale("1", 1.0, "20010103"), Sale("1", 0.5, "20010103"), Sale("2", 0.6, "20010103"),
        Sale("1", 0.1, "20010104"), Sale("1", 0.5, "20010104"), Sale("2", 0.6, "20010104"),
        Sale("1", 1.0, "20010105"), Sale("2", 0.8, "20010105"), Sale("2", 0.9, "20010105")
      )
      val dailyWinners = salesList.groupBy(_.dateTime.dateNumber).view.mapValues(_.groupBy(_.id).view.mapValues(_.map(_.amount).sum).maxBy(_._2)).toMap
      val dailyWinnersRecords = dailyWinners.map{case(date, (id, dailyAmount)) => DailySaleRecord(date, id, dailyAmount)}.toVector
      val myWinners = GenericTools.listBestHistoricalRecords[DailySaleRecord, DateNum, BigDecimal](dailyWinnersRecords, _.date, _.dailyAmount)
      myWinners shouldBe Vector(DailySaleRecord(20010102,"2",1.1), DailySaleRecord(20010103,"1",1.5), DailySaleRecord(20010105,"2",1.7))
    }

    "list all historical records in ad hoc manner" in {
      val salesList = List(Sale("1", 1.0, "20010101"),
        Sale("1", 1.0, "20010102"), Sale("2", 0.5, "20010102"), Sale("2", 0.6, "20010102"),
        Sale("1", 1.0, "20010103"), Sale("1", 0.5, "20010103"), Sale("2", 0.6, "20010103"),
        Sale("1", 0.1, "20010104"), Sale("1", 0.5, "20010104"), Sale("2", 0.6, "20010104"),
        Sale("1", 1.0, "20010105"), Sale("2", 0.8, "20010105"), Sale("2", 0.9, "20010105")
      )
      import GenericTools.implicits._
      val dailyWinners = salesList.groupBy(_.dateTime.dateNumber).view.mapValues(_.groupBy(_.id).view.mapValues(_.map(_.amount).sum).maxBy(_._2)).toMap
      val dailyWinnersRecords = dailyWinners.map{case(date, (id, dailyAmount)) => DailySaleRecord(date, id, dailyAmount)}.toVector
      val vecWinners = dailyWinnersRecords.listBestRecordsOrderBy(_.date).onRecordValue(_.dailyAmount)
      vecWinners shouldBe Vector(DailySaleRecord(20010102,"2",1.1), DailySaleRecord(20010103,"1",1.5), DailySaleRecord(20010105,"2",1.7))

      val arrWinners = dailyWinnersRecords.toArray.listBestRecordsOrderBy(_.date).onRecordValue(_.dailyAmount)
      arrWinners shouldBe Vector(DailySaleRecord(20010102,"2",1.1), DailySaleRecord(20010103,"1",1.5), DailySaleRecord(20010105,"2",1.7))

      val listWinners = dailyWinnersRecords.toArray.listBestRecordsOrderBy(_.date).onRecordValue(_.dailyAmount)
      listWinners shouldBe Vector(DailySaleRecord(20010102,"2",1.1), DailySaleRecord(20010103,"1",1.5), DailySaleRecord(20010105,"2",1.7))
    }
  }
}