package course3

trait MapTools[F[_], K, V] {
  def filterByValues(condition: V => Boolean)
                    (implicit f: F[V] => TraversableGeneric[F, V]): Map[K, F[V]]
  def partitionByValues(condition: V => Boolean)
                       (implicit f: F[V] => TraversableGeneric[F, V]): (Map[K, F[V]], Map[K, F[V]])
}
object MapTools {
  def filterMapByValues[F[_], K, V](map: Map[K, F[V]], condition: V => Boolean)
                                (implicit f: F[V] => TraversableGeneric[F, V]): Map[K, F[V]] = {
    map.map { case (key, values) =>
      key -> f(values).filter(condition)
    }.filter(_._2.nonEmpty)
  }
  def partitionMapByValues[F[_], K, V](map: Map[K, F[V]], condition: V => Boolean)
                                   (implicit f: F[V] => TraversableGeneric[F, V]): (Map[K, F[V]], Map[K, F[V]]) = {
    (filterMapByValues(map, condition), filterMapByValues(map, (v: V) => !condition(v)))
  }
  object implicits {
    implicit class mapHasTools[F[_], K, V](map: Map[K, F[V]]) extends MapTools[F, K, V] {
      override def filterByValues(condition: V => Boolean)
                                 (implicit f: F[V] => TraversableGeneric[F, V]): Map[K, F[V]] = {
        filterMapByValues(map, condition)
      }

      override def partitionByValues(condition: V => Boolean)
                                    (implicit f: F[V] => TraversableGeneric[F, V]): (Map[K, F[V]], Map[K, F[V]]) = {
        partitionMapByValues(map, condition)
      }
    }
  }
}
