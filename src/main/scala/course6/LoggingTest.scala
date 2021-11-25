package course6

import tools.ziologging
import zio._
import zio.duration._
object LoggingTest extends App {
  /*

  val prog2 = for {
    logging <- ZIO.service[Logging[String]]
    me <- ZIO.succeed(23)
    _ <- log("Hello World")
    _ <- log("It's me!")
    two <- ZIO.effect(me / 0)
    _ <- log("It's Ok!")
    //logs <- logging.logger.get
    //_ <- putStrLn(logs.mkString("\n"))
  } yield ()*/

  import zio.logging._

  val zlog = ziologging.LoggerTool.env

  //val WilliamImpl = ziologging.live


  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val saleShare = (number: Int) => for {
      _ <- log.info(s"User sale $number shares")
      _ <- log.debug(s"sale $number shares").delay(5 seconds)
      originalShares <- IO.effect {
        /* Let's say retrieve from DB */ 24
      }
      _ <- log.debug(s"Retrieve from database, user has $originalShares shares")
      restOfShares <- ZIO.succeed(originalShares - number)
      _ <- log.debug(s"User has $restOfShares shares left in account")
      validated <- Task.effect(if (restOfShares < 0) throw new RuntimeException("You don't have so many!") else restOfShares)
      _ <- log.debug("function Done")
    } yield validated

    saleShare(2).provideCustomLayer(zlog).ignore.exitCode
  }
}
