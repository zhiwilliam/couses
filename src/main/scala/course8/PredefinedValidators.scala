package course8

import StrNelValidator._

object PredefinedValidators {
  val nonEmptyStr: Validator[StrNel, String] = (value: String) => if (value.isEmpty) Left("cannot be empty.") else Right(value)
  val notNullStr: Validator[StrNel, String] = (value: String) => if (value == null) Left("cannot be null.") else Right(value)
  val limitLenStr: Int => Validator[StrNel, String] = lengthLimit => (value: String) =>
    if (value.length > lengthLimit) Left(s"length cannot over $lengthLimit, but it is ${value.length} now.") else Right(value)
}
