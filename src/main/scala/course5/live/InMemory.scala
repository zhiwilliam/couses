package course5.live

import cats.effect.IO
import course5.services.StreamReader

object InMemory {
  implicit object IntAsStream extends StreamReader[Int] {
    import course5.common.mkStringAsStream
    override def read(): fs2.Stream[IO, Int] = mkStringAsStream(List(1, 2, 3, 4, 5))
  }
}
