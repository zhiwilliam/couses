package timetools

import cats._
import cats.data._
import cats.implicits._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import java.util.Date

import scala.util.Try

trait ValidatedDateTime[A] extends DateTimeWrapper[A] with Validating[DateTime] {
  type VE[K] = ValidatedNec[Throwable, K]
  override def validate: Validated[Throwable, DateTime] = Try(dateValue).toValidated

  private def validCompare[B](that : ValidatedDateTime[B])(f: (DateTime, DateTime) => Boolean): ValidatedNec[Throwable, Boolean] =
    Apply[VE].map2(validate.toValidatedNec, that.validate.toValidatedNec){
      case (thisDate, thatDate) => f(thisDate, thatDate)}

  def isBefore(that: ValidatedDateTime[A]): ValidatedNec[Throwable, Boolean] =
    validCompare(that)(_ isBefore _)

  def isAfter(that: ValidatedDateTime[A]): ValidatedNec[Throwable, Boolean] =
    validCompare(that)(_ isAfter _)

  def isSame(that: ValidatedDateTime[A]): ValidatedNec[Throwable, Boolean] =
    (isBefore(that), isAfter(that)) mapN (_ && _)
}

object ValidatedDateTime {
  implicit class JavaDateTime(date: java.util.Date) extends ValidatedDateTime[java.util.Date] {
    override val originValue: Date = date
    override lazy val dateValue: DateTime = new DateTime(date)
  }

  implicit class StringDateTime(date: String) extends ValidatedDateTime[String] {
    lazy val yyyyMMddFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
    override val originValue: String = date
    override lazy val dateValue: DateTime = date match {
      case x if x.length == 8 => yyyyMMddFormatter.parseDateTime(x)
    }
  }
}