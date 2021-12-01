package course8

import cats.ApplicativeError
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._

import scala.language.implicitConversions
import scala.reflect.ClassTag

object StrNelValidator {

  import cats.data._

  type StrNel[A] = ValidatedNel[String, A]

  implicit val ae: ApplicativeError[StrNel, String] = new ApplicativeError[StrNel, String] {

    import cats.derived.auto.iterable.kittensMkIterable

    def pure[A](x: A): StrNel[A] = Validated.valid(x)

    def handleErrorWith[A](fa: StrNel[A])(f: String => StrNel[A]): StrNel[A] = fa match {
      case Invalid(e) =>
        val (valid, invalid) = e.map(f).partition(_.isInvalid)
        if (invalid.nonEmpty)
          Invalid(NonEmptyList.fromList(invalid.flatMap { case Invalid(e) => e }.toList).get)
        else valid.head
      case x => x
    }

    def raiseError[A](e: String): StrNel[A] = Validated.Invalid(NonEmptyList.of(e))

    def ap[A, B](ff: StrNel[A => B])(fa: StrNel[A]): StrNel[B] = fa ap ff
  }

  implicit def makeValidator[A: ClassTag](validateFunc: A => Either[String, A]): Validator[StrNel, A] = {
    new Validator[StrNel, A] { self =>
      override lazy val valueName: String = implicitly[ClassTag[A]].runtimeClass.getSimpleName

      override def validate(value: A)(implicit ae: ApplicativeError[StrNel, String]): StrNel[A] =
        validateFunc(value) match {
          case Left(e) => Invalid(NonEmptyList.of(s"$valueName $e"))
          case Right(a) => Valid(a)
        }
    }
  }

  implicit class makeValidatorOps[A](validator: Validator[StrNel, A]) extends ValidatorOps[StrNel, A] { self =>
    override def lift[B: ClassTag](f: B => A, fieldName: String)(implicit getKeyInfo: B => String): Validator[StrNel, B] = new Validator[StrNel, B] {
      override lazy val valueName: String = implicitly[ClassTag[B]].runtimeClass.getSimpleName

      override def validate(b: B)(implicit ae: ApplicativeError[StrNel, String]): StrNel[B] =
        validator.validate(f(b)) match {
          case Invalid(e) =>
            val keyInfo = getKeyInfo(b)
            if(keyInfo.isEmpty)
              Invalid(e.map(error => s"$valueName's $fieldName ${error.split(" ").tail.mkString(" ")}"))
            else
              Invalid(e.map(error => s"$valueName ($keyInfo)'s $fieldName ${error.split(" ").tail.mkString(" ")}"))
          case Valid(_) => ae.pure(b)
        }
    }

    override def >>+(other: Validator[StrNel, A]): Validator[StrNel, A] = new Validator[StrNel, A] {
      override lazy val valueName: String = validator.valueName

      override def validate(value: A)(implicit ae: ApplicativeError[StrNel, String]): StrNel[A] =
        validator.validate(value) match {
          case Valid(a) => other.validate(a)
          case e => e
        }
    }

    override def <<+(other: Validator[StrNel, A]): Validator[StrNel, A] = new Validator[StrNel, A] {
      override lazy val valueName: String = validator.valueName

      override def validate(value: A)(implicit ae: ApplicativeError[StrNel, String]): StrNel[A] = {
        validator.validate(value) <+> other.validate(value)
      }
    }

    override def <+>(other: Validator[StrNel, A]): Validator[StrNel, A] = new Validator[StrNel, A] {
      override lazy val valueName: String = validator.valueName

      override def validate(value: A)(implicit ae: ApplicativeError[StrNel, String]): StrNel[A] = {
        (validator.validate(value), other.validate(value)) match {
          case (Valid(_), Valid(_)) => Valid(value)
          case (Invalid(e), Valid(_)) => Invalid(e)
          case (Valid(_), Invalid(e)) => Invalid(e)
          case (Invalid(e1), Invalid(e2)) => Invalid(e1 |+| e2)
        }
      }
    }
  }
}
