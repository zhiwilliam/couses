package course9

import eu.timepit.refined.boolean._
import eu.timepit.refined.string.MatchesRegex

object RefineTest extends App {
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.auto._
  import eu.timepit.refined.numeric._
  import eu.timepit.refined.W
  import eu.timepit.refined.string._
  //import PlayJson._
  import be.venneborg.refined.play.RefinedJsonFormats._

  type Email = String Refined MatchesRegex[W.`"""[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]{2,}"""`.T]
  type Age = Int Refined And[Not[Less[0]], Not[Greater[150]]]
  type Name = String Refined MatchesRegex[W.`"""[A-Z][a-z]+"""`.T]

  case class Profile(name: Name, age: Age, email: Email)

  //val myCase = Profile("232", 23, "sdfsdf")

  import play.api.libs.json.Json
  implicit val developerFormat = Json.format[Profile]

  val profileWrong = Json.obj(
    "name" -> "1111 2222 3333 4444",
    "age" -> 2500,
    "email" -> "666"
  )

  val profile = Json.obj(
    "name" -> "Bill",
    "age" -> 55,
    "email" -> "billgates@gmail.com"
  )
  println(Json.fromJson[Profile](profile))


}
