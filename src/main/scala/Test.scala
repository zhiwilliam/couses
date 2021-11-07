import binarytreetools.Node

object Test extends App {


  //val tree = Node("1", Node("2", Node("4"), Node("5")), Node("3"))
  //val tree = Node("1", Node("2", Node("2.1"), Node("2.2")), Node("3"))
  //val tree = Node("1", Node("123"), Node("456"))
  //val tree = Node("1", null, Node("2", null, Node("3")))
  //val tree = Node("1", Node("2", Node("3"), null), null)
  val tree = Node("1", Node("2", Node("4"), Node("5")), Node("3", Node("0", Node("9"), null), Node("6", Node("7"), null)))
  import binarytreetools.Actions._

  println(tree.dsfTreeTraverse[Vector, String](DsfOrders.Inorder,
    (node, result) => result :+ node.value
  ).mkString(","))

  println(tree.dsfTreeTraverse[Option, String](DsfOrders.Postorder,
    (node, result) => result match {
      case None => Option(node.value)
      case Some(max) => if (node.value > max) Option(node.value) else Option(max)
    }
  ))

  val value: Unit = tree.prettyPrint()
}



  /*trait NodeOps[F[_], A] {
    def processNode(node: Node[A], result: F[A]): F[A]
  }


  def dsfExtendTree[F[_], A](tree: Node[A], order: DsfOrder, result: F[A])(implicit nodeOps: NodeOps[F, A]): F[A] = {
    if (tree == null) result
    else {
      order match {
        case Preorder =>
          val nodeRes = nodeOps.processNode(tree, result)
          val leftRes = dsfExtendTree(tree.lt, order, nodeRes)
          val rightRes = dsfExtendTree(tree.rt, order, leftRes)
          rightRes
        case Inorder =>
          val leftRes = dsfExtendTree(tree.lt, order, result)
          val nodeRes = nodeOps.processNode(tree, leftRes)
          val rightRes = dsfExtendTree(tree.rt, order, nodeRes)
          rightRes
        case Postorder =>
          val leftRes = dsfExtendTree(tree.lt, order, result)
          val rightRes = dsfExtendTree(tree.rt, order, leftRes)
          val nodeRes = nodeOps.processNode(tree, rightRes)
          nodeRes
      }
    }
  }

  implicit val extendIntNodeOps: NodeOps[Array, Int] = (node, result) => result :+ node.value
  println(dsfExtendTree[Array, Int](tree, Postorder, Array[Int]()).mkString(", "))
  */
  /*def dsfExtendTree[F[_], A](tree: Node[A], order: DsfOrder, checkAndAppend: (Node[A], F[A]) => F[A], result: F[A]): F[A] = {
    if (tree == null) result
    else {
      order match {
        case Preorder =>
          val nodeRes = checkAndAppend(tree, result)
          val leftRes = dsfExtendTree(tree.lt, order, checkAndAppend, nodeRes)
          val rightRes = dsfExtendTree(tree.rt, order, checkAndAppend, leftRes)
          rightRes
        case Inorder =>
          val leftRes = dsfExtendTree(tree.lt, order, checkAndAppend, result)
          val nodeRes = checkAndAppend(tree, leftRes)
          val rightRes = dsfExtendTree(tree.rt, order, checkAndAppend, nodeRes)
          rightRes
        case Postorder =>
          val leftRes = dsfExtendTree(tree.lt, order, checkAndAppend, result)
          val rightRes = dsfExtendTree(tree.rt, order, checkAndAppend, leftRes)
          val nodeRes = checkAndAppend(tree, rightRes)
          nodeRes
      }
    }
  }*/

