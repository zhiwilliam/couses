package course8

import StrNelValidator._

object PredefinedValidators {
  val nonEmptyStr: Validator[StrNel, String] = makeValidator(value => if (value.isEmpty) Left("cannot be empty.") else Right(value))
  val notNullStr: Validator[StrNel, String] = makeValidator(value => if (value == null) Left("cannot be null.") else Right(value))
  val limitLenStr: Int => Validator[StrNel, String] = lengthLimit => makeValidator( value =>
    if (value.length > lengthLimit) Left(s"length is over $lengthLimit.") else Right(value))
}
