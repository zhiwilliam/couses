package course5.live

import cats.effect._
import course5.services.StreamReader

object CsvFile {
  implicit object IntAsStream extends StreamReader[Int] {
    import course5.common._
    import fs2.data.csv._
    case class MyRow(id: Int)

    implicit object MyRowDecoder extends RowDecoder[MyRow] {
      def apply(row: Row): DecoderResult[MyRow] =
        for {
          id <- CellDecoder[Int].apply(row.values.head)
        } yield MyRow(id)
    }
    override def read(): fs2.Stream[IO, Int] = {
      val textStream = mkCsvAsStreamUtf8("C:\\temp\\test\\input.csv")
      textStream.through(decodeSkippingHeaders[MyRow]()).map(_.id)
    }
  }
}
