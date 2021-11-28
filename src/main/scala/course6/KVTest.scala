package course6

import tools.ziologging
import zio.console.putStrLn
import zio.logging.log
import zio._

object KVTest extends App {
  //import tools.zioredis._
  import tools.cache.lru._
  val prog = for {
    _ <- putInt(23, 32)
    value <- getInt(23)
    _ <- log.info(s"found $value")
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    prog.provideSomeLayer(
      (ZLayer.succeed(2) >>> IntLRUCacheEnv.Service.zioRefImpl) ++ ziologging.live
      //ziologging.live >+> executor
    )
      .catchAll(ex => putStrLn(ex.getMessage) *> ZIO.succeed(1)).exitCode
  }
}
