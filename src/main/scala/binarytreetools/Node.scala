package binarytreetools

case class Node[A](value: A, lt: Node[A], rt: Node[A])

object Node {
  def apply[A](value: A): Node[A] = Node(value, null, null)
}
