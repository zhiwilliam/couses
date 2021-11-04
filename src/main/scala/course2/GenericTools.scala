package course2

import scala.language.implicitConversions
import scala.math.Ordering.Implicits._

object GenericTools {
  def listBestHistoricalRecords[A, B, C](data: Vector[A], fGetOrder: A => B, fGetValue: A => C)
                                        (implicit compareTime: Ordering[B], compareValue: Ordering[C]): Vector[A] = {
    val historicalOrderedRecords = data.sortBy(fGetOrder)
    historicalOrderedRecords.headOption.toVector.flatMap { head =>
      val recWinners = historicalOrderedRecords.foldLeft((head, Vector[A]())) {
        case ((rec, winners), next) =>
          if (fGetValue(next) > fGetValue(rec)) {
            (next, winners :+ next )
          }
          else (rec, winners)
      }
      recWinners._2
    }
  }


  trait DSLArrayHistoricalRecords[A] {
    val data: Vector[A]
    def listBestRecordsOrderBy[B](fGetOrder: A => B) = new {
      def onRecordValue[C](fGetValue: A => C)
                          (implicit compareValue: Ordering[B], compareTime: Ordering[C]): Vector[A] =
        listBestHistoricalRecords(data, fGetOrder, fGetValue)
    }
  }

  object implicits {
    implicit def arrToHisRec[T](arr: Array[T]): DSLArrayHistoricalRecords[T] = new DSLArrayHistoricalRecords[T] {
      override val data: Vector[T] = arr.toVector
    }

    implicit def vecToHisRec[T](arr: Vector[T]): DSLArrayHistoricalRecords[T] = new DSLArrayHistoricalRecords[T] {
      override val data: Vector[T] = arr
    }

    implicit def listToHisRec[T](arr: List[T]): DSLArrayHistoricalRecords[T] = new DSLArrayHistoricalRecords[T] {
      override val data: Vector[T] = arr.toVector
    }
  }

}
