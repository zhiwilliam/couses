package course8

object ValidatorExample extends App {
  case class Profile(id: String, address: Address)
  case class Address(street: String, country: String)

  import PredefinedValidators._
  import StrNelValidator._
  implicit def ignoreKey[A]: A => String = (a: A) => ""

  val validateStreet = notNullStr >>+ nonEmptyStr >>+ limitLenStr(200)
  val validateCountry = notNullStr >>+ nonEmptyStr >>+ limitLenStr(10)

  val validateAddress = validateStreet.lift[Address](_.street, "street") <+>
                        validateCountry.lift[Address](_.country, "country")

  implicit val getKeyInfo2: Profile => String = p => s"ID is ${p.id}"
  val validateProfile = validateAddress.lift[Profile](_.address, "address")

  println(validateProfile.validate(Profile("12", Address(null, "sdfsdfsadfsadfsad"))))
}
