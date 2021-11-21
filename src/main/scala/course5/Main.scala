package course5

import cats.effect._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import live._
    import live.CsvFile._

    import cats.effect.unsafe.implicits.global

    val data = liveStream[Int]()/*.evalMap{ x =>
      /*for {
        fiber <- IO(...something like query by Id ...).fork
        ... <- fiber.join
      } yield {
      }*/
      x
    }*/.compile.toVector.unsafeRunSync()
    println(data)
    IO(ExitCode.Success)
  }
}
