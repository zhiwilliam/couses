package course6

import tools.cache.fixed.FixedCache
import zio._
import zio.config._
import magnolia.DeriveConfigDescriptor._
import tools.zioconfig._
import zio.console.{Console, putStrLn}

object Test extends App {
  case class HttpQuery(uri: String, query: String)
  case class ApplicationConfig(demo: HttpQuery)
  val appconf = descriptor[ApplicationConfig]

  // 主业务逻辑，读出代码并打印
  val businessLogic = for {
    appConfig <- getConfig[ApplicationConfig]
    _         <- putStrLn(appConfig.demo.uri)
    _         <- putStrLn(appConfig.demo.query)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    // 从Evironment variable 里面读配置的做法。
    val config = readDescription(appconf).provideLayer(readFromSysEnv().toLayer)
    /*lazy val configCache = (for {
      appConfig <- ZIO.service[ApplicationConfig]
      appConfig2 <- ZIO.service[ApplicationConfig]
      cache <- FixedCache.make(Map("appConfig" -> appConfig, "appConfig2" -> appConfig2))
    } yield cache).provideLayer(config.toLayer)
    configCache.flatMap(_.get("appConfig2")).toLayer
*/
    businessLogic.provideLayer(config.toLayer ++ Console.live).exitCode
  }
}
