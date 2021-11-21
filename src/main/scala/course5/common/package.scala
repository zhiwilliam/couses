package course5

import cats.effect._
import fs2.text

package object common {
  def mkStringAsStream[A](data: Seq[A]): fs2.Stream[IO, A] = fs2.Stream.eval(IO(data)).flatMap(fs2.Stream.emits(_))

  def mkCsvAsStreamUtf8(csvFilePath: String): fs2.Stream[IO, String] = {
    import fs2.io.file._
    Files[IO].readAll(Path(csvFilePath)).through(text.utf8.decode).through(text.lines).intersperse("\n")
  }
}
