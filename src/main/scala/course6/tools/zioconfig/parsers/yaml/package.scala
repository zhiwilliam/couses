package course6.tools.zioconfig.parsers

import java.io.InputStreamReader

import com.typesafe.config.ConfigFactory
import io.circe.yaml.parser
import zio._
import zio.config._
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

package object yaml {
  def read2ConfigSource: ZManaged[InputStreamReader, Throwable, ConfigSource] = {
    val configSource = for {
      streamReader <- ZIO.environment[InputStreamReader]
      parseJson <- Task.effect(parser.parse(streamReader))
      conf <- Task.effect(parseJson match {
        case Right(json) => ConfigFactory.parseString(json.toString)
        case Left(error) => throw new Exception(error.getMessage())
      })
      confSource <- Task.fromEither(fromTypesafeConfig(conf))
    } yield confSource
    ZManaged.fromEffect(configSource)
  }
}
