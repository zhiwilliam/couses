package course7

import cats.data._
import cats.implicits.toSemigroupKOps

import scala.reflect._

object Test extends App {
  def className[A : ClassTag]: String = {
    implicitly[ClassTag[A]].runtimeClass.getSimpleName
  }

  import cats._
  //import cats.implicits._

  def attemptDivideApplicativeError[F[_]](x: Int, y: Int)(implicit ae: ApplicativeError[F, String]): F[Int] = {
    if (y == 0) ae.raiseError("divisor is error")
    else {
      ae.pure(x/y)
    }
  }

  type MyValidated[A] = ValidatedNel[String, A]

  trait StringValidator[F[_]] {
    val value: String

    def notEmpty(implicit ae: ApplicativeError[F, String]): F[String] =
      if (value.isEmpty) ae.raiseError("value is empty") else ae.pure(value)

    def limitLength(length: Int)(implicit ae: ApplicativeError[F, String]): F[String] =
      if (value.length > length) ae.raiseError(s"string length over $length") else ae.pure(value)

  }

  implicit class StrValidator[F[_]](s: String) extends StringValidator[F] {
    override val value: String = s
  }

  case class Address(address: String, postalCode: String, countryCode: String, city: String, state: Option[String], country: String)


  def validateAddress(address: String) = address.notEmpty <+> address.limitLength(2)

  println(validateAddress(null))
}
