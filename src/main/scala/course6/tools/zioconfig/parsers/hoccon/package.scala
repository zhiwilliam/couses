package course6.tools.zioconfig.parsers

import java.io.InputStreamReader

import com.typesafe.config.ConfigFactory
import io.circe.yaml.parser
import zio._
import zio.config._
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

package object hoccon {
  def read2ConfigSource: ZManaged[InputStreamReader, Throwable, ConfigSource] = {
    val configSource = for {
      streamReader <- ZIO.environment[InputStreamReader]
      conf <- Task.effect(ConfigFactory.parseReader(streamReader))
      confSource <- Task.fromEither(fromTypesafeConfig(conf))
    } yield confSource
    ZManaged.fromEffect(configSource)
  }
}
