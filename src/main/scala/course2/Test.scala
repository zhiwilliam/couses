package course2

object Test extends App {
  import timetools.DateTimeWrapper._
  import scala.concurrent.duration.DurationInt
  println("2019-01-03" plus (3 days) isAfter "2019-01-05")
  println("2019-01-03" minus (1 minutes) isBefore "2019-01-03")
}
