package course6.tools.zioconfig.parsers

import java.io.InputStreamReader

import com.typesafe.config.ConfigFactory
import io.circe.yaml.parser
import zio._
import zio.config._

package object properties {
  private val properties = new java.util.Properties()

  def read2ConfigSource(keyDelimiter: Option[Char] = Some('.'),
                        valueDelimiter: Option[Char] = Some(',')): ZManaged[InputStreamReader, Throwable, ConfigSource] = {
    val configSource = for {
      streamReader <- ZIO.environment[InputStreamReader]
      _ <- Task.effect(properties.load(streamReader))
      confSource <- Task.effect(ConfigSource.fromProperties(properties,"properties",
        keyDelimiter, valueDelimiter, LeafForSequence.Valid))
    } yield confSource
    ZManaged.fromEffect(configSource)
  }
}
