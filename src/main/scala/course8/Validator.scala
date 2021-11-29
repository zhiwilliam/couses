package course8

import cats.ApplicativeError
import course8.StrNelValidator.StrNel

import scala.reflect.ClassTag

trait Validator[F[_], A] { self =>
  def valueName: String
  def validate(a: A)(implicit ae: ApplicativeError[F, String]): F[A]
}

trait ValidatorOps[F[_], A] {
  def lift[B: ClassTag](f: B => A, fieldName: String)(implicit getKeyInfo: B => String): Validator[F, B]
  def >>+ (other: Validator[F, A]): Validator[F, A]
  def <+> (nextValidator: Validator[StrNel, A]): Validator[StrNel, A]
}

