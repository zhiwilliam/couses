package course6

import course6.Test.ApplicationConfig
import zio.blocking.Blocking
import zio.console.putStrLn
import zio.stream.ZTransducer.{gzip, splitLines, utf8Decode}
import zio.stream.{ZSink, ZStream}
import zio.{App, ExitCode, URIO, ZIO, config}

import java.io.InputStream
import java.nio.file.Paths

object CrawlerTest extends App {
  val businessLogic = for {
    appConfig <- config.getConfig[ApplicationConfig]
    _ <- putStrLn(appConfig.demo.uri)
    _ <- putStrLn(appConfig.demo.query)
  } yield ()

  val ks = for {
    _ <- ZStream.fromIterable(Seq(1, 2, 3, 4))
      .map(value => s"$value")
      .map(value => value.toByte)
      .transduce(gzip())
      .run(ZSink.fromFile(Paths.get("file.txt")))
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    def prog = for {
      is <- ZIO.service[InputStream]
      lines <- ZStream
        .fromInputStream(is)
        .transduce(utf8Decode >>> splitLines)
        .filter(_.trim.nonEmpty)
        //.mapMPar(10)(parse to object)
        //.tap(save to db)
        .runCollect
      //.map(value => value.toByte)
      //.run(ZSink.fromFile(Paths.get("file.txt")))
    } yield lines

    prog.provideSomeLayer(
      Blocking.live >+> tools.http.client.live("https://finnhub.io/api/v1/quote?symbol=AAPL").toLayer).exitCode
  }
}
