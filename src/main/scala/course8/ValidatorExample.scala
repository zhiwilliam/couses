package course8

object ValidatorExample extends App {
  case class Deliver(item: String, address: Address)
  case class Profile(id: String, address: Address)
  case class Address(streetLine1: String, streetLine2: String, country: String)

  import PredefinedValidators._
  import StrNelValidator._
  implicit def ignoreKey[A]: A => String = (a: A) => ""

  val validateStreet = notNullStr >>+ nonEmptyStr >>+ limitLenStr(200)
  val validateCountry = notNullStr >>+ nonEmptyStr >>+ limitLenStr(10)

  // Here we have some special requirements says address line 1 cannot be same as address line 2
  val street1NotSameAs2: Validator[StrNel, Address] =
    (address: Address) =>
      if (address.streetLine1 == address.streetLine2)
        Left("street line 1 cannot be same as street line 2.")
      else
        Right(address)

  val validateAddress: Validator[StrNel, Address] =
                      ((validateStreet.lift[Address](_.streetLine1, "streetLine1") <+>
                        validateStreet.lift[Address](_.streetLine2, "streetLine2")
                        ) >>+ street1NotSameAs2
                       ) <+> validateCountry.lift[Address](_.country, "country")

  implicit val additionalKeyInfo: Profile => String = p =>s"ID is ${p.id}"
  val validateProfile = validateAddress.lift[Profile](_.address, "address")

  val validateDeliver = validateAddress.lift[Deliver](_.address, "address")

  println(validateProfile.validate(Profile("12",
    Address(null, "line2", "some super long string that cannot be a country code."))))

  println(validateProfile.validate(Profile("12",
    Address("line2", "line2", "some super long string that cannot be a country code."))))

  println(validateDeliver.validate(Deliver("tool",
    Address("line2", "line2", null))))


}
