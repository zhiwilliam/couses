package course6.tools

import java.io._

import zio._

import scala.io.BufferedSource

package object source {
  type StreamReader = Has[InputStreamReader]

  val streamReader: ZManaged[InputStream, Throwable, InputStreamReader] =
    ZManaged.fromAutoCloseable(ZIO.environment[InputStream] >>= (inputStream => IO.effect(new InputStreamReader(inputStream))))

  val bufferedReader: ZManaged[BufferedSource, Throwable, InputStreamReader] =
    ZManaged.fromAutoCloseable(ZIO.environment[BufferedSource] >>= (buffer => IO.effect(buffer.reader())))
}
