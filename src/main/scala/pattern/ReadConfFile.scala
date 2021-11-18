package pattern

import cats.effect._

import java.io._


/*object ReadConfFile extends IOApp {
  def inputStream(f: File): Resource[IO, FileInputStream] =
    Resource.make {
      IO.blocking(new FileInputStream(f))                         // build
    } { inStream =>
      IO.blocking(inStream.close()).handleErrorWith(_ => IO.unit) // release
    }

  override def run(args: List[String]): IO[ExitCode] = {
    val ss = inputStream(new File("c:\\temp\\test.txt")).use[FileInputStream]{ input =>
      IO(input)}

    ss
  }

}*/
