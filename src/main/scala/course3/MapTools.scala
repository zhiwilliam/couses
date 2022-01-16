package course3

object MapTools {
  def filterMapByValues[F[_], K, V](map: Map[K, F[V]], condition: V => Boolean)
                                (implicit iter: F[V] => TraversableGeneric[F, V]): Map[K, F[V]] = {
    map.map { case (key, values) =>
      key -> iter(values).filter(condition)
    }.filter(_._2.nonEmpty)
  }
  def partitionMapByValues[F[_], K, V](map: Map[K, F[V]], condition: V => Boolean)
                                   (implicit iter: F[V] => TraversableGeneric[F, V]): (Map[K, F[V]], Map[K, F[V]]) = {
    (filterMapByValues(map, condition), filterMapByValues(map, (v: V) => !condition(v)))
  }
  object implicits {
    trait MapFilter[F[_], K, V] {
      def map: Map[K, F[V]]
      def filterByValues(condition: V => Boolean)
                        (implicit iter: F[V] => TraversableGeneric[F, V]): Map[K, F[V]] = {
        filterMapByValues(map, condition)
      }
      def partitionByValues(condition: V => Boolean)
                           (implicit iter: F[V] => TraversableGeneric[F, V]): (Map[K, F[V]], Map[K, F[V]]) = {
        partitionMapByValues(map, condition)
      }
    }
    implicit class mapWithTraversableValuesFilter[F[_], K, V](toBeFilteredMap: Map[K, F[V]]) extends MapFilter[F, K, V] {
      override def map: Map[K, F[V]] = toBeFilteredMap
    }
  }
}
