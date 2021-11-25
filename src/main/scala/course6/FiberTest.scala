package course6

import zio.console.{Console, putStrLn}
import zio._

object FiberTest extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val t2 = for {
      _ <- putStrLn("t1 started")
      _ <- putStrLn("t1 stopped")
    } yield ()
    val t3 = for {
      _ <- putStrLn("t3 started")
      _ <- Task.effect {
        Thread.sleep(1000)
      }
      _ <- putStrLn("t3 stopped")
    } yield ()

    val a = for {
      fb <- ZIO.ifM(ZIO.succeed(false))(t3.fork, t2.fork)
      _ <- fb.join
    } yield ()

    a.provideLayer(Console.live).exitCode
  }
}
