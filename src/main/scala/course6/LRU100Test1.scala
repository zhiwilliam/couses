package course6

import course6.tools.cache.lru._
import zio.console.{Console, getStrLn, putStrLn}
import zio.random.{Random, nextIntBounded}
import zio._

object LRU100Test1 extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val producers = ZIO.replicate(100)(producer.forever)
    val consumers = ZIO.replicate(100)(consumer.forever)
    (for {
      fiberReporter <- reporter.forever.fork
      fiberProducers <- ZIO.forkAll(producers.map(_.forkDaemon))
      fiberConsumers <- ZIO.forkAll(consumers.map(_.forkDaemon))
      _ <- getStrLn.orDie *> (fiberReporter <*> fiberProducers <*> fiberConsumers).interrupt
    } yield 0)
      .provideCustomLayer(ZLayer.succeed(3) /* 设置LRU缓存的大小*/ >>> IntLRUCacheEnv.Service.zioRefImpl)
      .catchAll(ex => putStrLn(ex.getMessage) *> ZIO.succeed(1)).exitCode
  }

  val producer: URIO[Console with Random with IntLRUCacheEnv, Unit] =
    for {
      number <- nextIntBounded(100)
      _ <- putStrLn(s"Producing ($number, $number)")
      _ <- putInt(number, number)
    } yield ()

  val consumer: URIO[Console with Random with IntLRUCacheEnv, Unit] =
    (for {
      key <- nextIntBounded(100)
      _ <- putStrLn(s"Consuming key: $key")
      value <- getInt(key)
      _ <- putStrLn(s"Consumed value: $value")
    } yield ()).catchAll(ex => putStrLn(ex.getMessage))

  val reporter: ZIO[Console with IntLRUCacheEnv, NoSuchElementException, Unit] =
    for {
      (items, optionStart, optionEnd) <- getCacheStatus
      _ <- putStrLn(s"Items: $items, Start: $optionStart, End: $optionEnd")
    } yield ()
}
