package course5

import course5.services.StreamReader

package object live {
  import cats.effect.IO
  def liveStream[A: StreamReader](): fs2.Stream[IO, A] = {
    val reader = implicitly[StreamReader[A]]
    for {
      stream <- reader.read()
    } yield stream
  }
}
