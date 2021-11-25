package course6.tools.source.from

import java.io.FileInputStream

import zio._

import scala.io._

package object file {
  val fileSource = (filePath: String) => ZManaged.fromAutoCloseable(IO.effect(Source.fromFile(filePath)))
  val resourceSource = (resourcePath: String) => ZManaged.fromAutoCloseable(IO.effect(Source.fromResource(resourcePath)))
  val resourceInputStream = (resourcePath: String) =>
    ZManaged.fromAutoCloseable(IO.effect{
      val fixedPath = if(!resourcePath.startsWith("/")) "/" + resourcePath else resourcePath
      val stream = getClass.getResourceAsStream(fixedPath)
      stream
    })
}
