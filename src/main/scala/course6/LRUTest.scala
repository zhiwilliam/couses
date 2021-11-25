package course6

import tools.cache.lru._
import zio.console.{Console, putStrLn}
import zio._

object LRUTest extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    (for {
      _ <- put(1, 1)
      _ <- put(2, 2)
      _ <- get(1)
      _ <- put(3, 3)
      _ <- get(2)
      _ <- put(4, 4)
      _ <- get(1)
      _ <- get(3)
      _ <- get(4)
    } yield 0)
      .provideCustomLayer(ZLayer.succeed(2) >>> IntLRUCacheEnv.Service.zioRefImpl)
      .catchAll(ex => putStrLn(ex.getMessage) *> ZIO.succeed(1)).exitCode
  }

  private def get(key: Int): URIO[Console with IntLRUCacheEnv, Unit] =
    (for {
      _ <- putStrLn(s"Getting key: $key")
      v <- getInt(key)
      _ <- putStrLn(s"Obtained value: $v")
    } yield ()).catchAll(ex => putStrLn(ex.getMessage))

  private def put(key: Int, value: Int): URIO[Console with IntLRUCacheEnv, Unit] =
    putStrLn(s"Putting ($key, $value)") *> putInt(key, value)
}
