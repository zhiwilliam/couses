package course3

import scala.reflect.ClassTag

trait TraversableGeneric[F[_], A] {
  def isEmpty: Boolean
  def nonEmpty: Boolean = !isEmpty
  def filter(condition: A => Boolean): F[A]
}
object TraversableGeneric {
  object implicits {
    implicit class listTraversable[A](values: List[A]) extends TraversableGeneric[List, A]  {
      override def isEmpty: Boolean = values.isEmpty
      override def filter(condition: A => Boolean): List[A] = values.filter(condition)
    }
    implicit class vectorTraversable[A](values: Vector[A]) extends TraversableGeneric[Vector, A]  {
      override def isEmpty: Boolean = values.isEmpty
      override def filter(condition: A => Boolean): Vector[A] = values.filter(condition)
    }
    implicit class setTraversable[A](values: Set[A]) extends TraversableGeneric[Set, A]  {
      override def isEmpty: Boolean = values.isEmpty
      override def filter(condition: A => Boolean): Set[A] = values.filter(condition)
    }
  }
}