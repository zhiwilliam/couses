package course2

import scala.language.implicitConversions
import scala.math.Ordering.Implicits.infixOrderingOps
import scala.reflect.ClassTag

object GenericTools {
  def listBestHistoricalRecords[A: ClassTag, B, C](data: Array[A], fGetTime: A => B, fGetValue: A => C)
                                                  (implicit compareTime: Ordering[B], compareValue: Ordering[C]): Array[A] =
    data.sortBy(fGetTime) match {
      case Array() => Array.empty[A]
      case arr =>
        val (_, winners) = arr.foldLeft((arr.head, Array[A]())) {
          case ((rec, winners), next) =>
            if (fGetValue(next) > fGetValue(rec)) {
              (next, winners :+ next )
            }
            else (rec, winners)
        }
        winners
    }


  trait DSLArrayHistoricalRecords[A] {
    val data: Array[A]
    def listBestRecords[B](fGetValue: A => B) = new {
      def of[C](fGetTime: A => C)(implicit T: ClassTag[A], compareValue: Ordering[B], compareTime: Ordering[C]): Array[A] =
        listBestHistoricalRecords(data, fGetTime, fGetValue)
    }
  }

  object implicits {
    implicit def arrToHisRec[T](arr: Array[T]): DSLArrayHistoricalRecords[T] = new DSLArrayHistoricalRecords[T] {
      override val data: Array[T] = arr
    }

    implicit def listToHisRec[T: ClassTag](arr: List[T]): DSLArrayHistoricalRecords[T] = new DSLArrayHistoricalRecords[T] {
      override val data: Array[T] = arr.toArray
    }
  }

}
