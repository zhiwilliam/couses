package course8

import cats.ApplicativeError

import scala.reflect.ClassTag

trait Validator[F[_], A] { self =>
  def valueName: String
  def validate(a: A)(implicit ae: ApplicativeError[F, String]): F[A]
}

// Defined all additional operations of a validator. So that we can assemble them to create stronger validator.
trait ValidatorOps[F[_], A] {
  def lift[B: ClassTag](f: B => A, fieldName: String)(implicit getKeyInfo: B => String): Validator[F, B]
  // Find any invalid then return invalid immediately
  def >>+ (other: Validator[F, A]): Validator[F, A]
  // If any validator proved valid, then return valid
  def <<+ (other: Validator[F, A]): Validator[F, A]
  // Find all invalid items and return them back together.
  def <+> (nextValidator: Validator[F, A]): Validator[F, A]
}

