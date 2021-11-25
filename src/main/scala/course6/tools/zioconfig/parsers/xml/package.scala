package course6.tools.zioconfig.parsers

import java.io.{InputStream, InputStreamReader}

import zio._
import zio.config._

package object xml {
  private val properties = new java.util.Properties()

  def read2ConfigSource(): ZManaged[InputStream, Throwable, ConfigSource] = {
    val configSource = for {
      inputStream <- ZIO.environment[InputStream]
      _ <- Task.effect(properties.loadFromXML(inputStream))
      confSource <- Task.effect(ConfigSource.fromProperties(properties,"properties",
        Some('.'), Some(','), LeafForSequence.Valid))
    } yield confSource
    ZManaged.fromEffect(configSource)
  }
}
