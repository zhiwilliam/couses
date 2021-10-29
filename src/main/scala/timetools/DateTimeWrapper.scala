package timetools

import org.joda.time.DateTime
import org.joda.time.format._
import timetools.DateTimeWrapper.DateNum

import scala.util.matching.Regex

trait DateTimeWrapper[A] {
  def originValue: A
  def dateValue: DateTime
  def isBefore[B](that: DateTimeWrapper[B]): Boolean = this.dateValue.isBefore(that.dateValue)
  def isAfter[B](that: DateTimeWrapper[B]): Boolean = this.dateValue.isAfter(that.dateValue)
  def isSame[B](that: DateTimeWrapper[B]): Boolean = !isBefore(that) && !isAfter(that)
  lazy val dateNumber: DateNum = dateValue.getYear * 10000 + dateValue.getMonthOfYear * 100 + dateValue.getDayOfMonth
}

object DateTimeWrapper {
  type DateNum = Int
  implicit class JavaDateTime(date: java.util.Date) extends DateTimeWrapper[java.util.Date] {
    override val originValue: java.util.Date = date
    override lazy val dateValue: DateTime = new DateTime(date)
  }

  implicit class StringDateTime(date: String) extends DateTimeWrapper[String] {
    lazy val yyyyMMddFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
    lazy val yyyy_MM_ddFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd")
    //lazy val StandardPattern1: Regex = """\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d(?:\.\d+)?Z?""".r
    //"2004-12-13T21:39:45.618-08:00"
    //lazy val StandardPattern1: Regex = """^(?:[\+-]?\d{4}(?!\d{2}\b))(?:(-?)(?:(?:0[1-9]|1[0-2])(?:\1(?:[12]\d|0[1-9]|3[01]))?|W(?:[0-4]\d|5[0-2])(?:-?[1-7])?|(?:00[1-9]|0[1-9]\d|[12]\d{2}|3(?:[0-5]\d|6[1-6])))(?:[T\s](?:(?:(?:[01]\d|2[0-3])(?:(:?)[0-5]\d)?|24\:?00)(?:[\.,]\d+(?!:))?)?(?:\2[0-5]\d(?:[\.,]\d+)?)?(?:[zZ]|(?:[\+-])(?:[01]\d|2[0-3]):?(?:[0-5]\d)?)?)?)?$""".r
    private lazy val StandardPattern1: Regex = "([\\d]{4}-[\\d]{2}-[\\d]{2}T[\\d]{2}:[\\d]{2})".r.unanchored
    override val originValue: String = date
    override lazy val dateValue: DateTime = date match {
      case StandardPattern1(_) => new DateTime(date)
      case x if x.length == 8 => yyyyMMddFormatter.parseDateTime(x)
      case x if x.length == 10 => yyyy_MM_ddFormatter.parseDateTime(x)
      case _ => null
    }
  }

  implicit class SQLDateTime(date: java.sql.Date) extends DateTimeWrapper[java.sql.Date] {
    override val originValue: java.sql.Date = date
    override lazy val dateValue: DateTime = new DateTime(date)
  }
}