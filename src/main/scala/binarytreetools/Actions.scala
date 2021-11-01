package binarytreetools

import cats.MonoidK

import scala.reflect.ClassTag

object Actions {
  object DsfOrders extends Enumeration {
    type DsfOrder = Value
    val Preorder, Inorder, Postorder = Value
  }

  import DsfOrders._

  trait TreeOps[A] {
    implicit val aTag: ClassTag[A]
    def dsfExtendTree[F[_]](tree: Node[A], order: DsfOrder, processNode: (Node[A], F[A]) => F[A])
                            (implicit emptyContainer: MonoidK[F]): F[A] = {
      def dsf(tree: Node[A], order: DsfOrder, result: F[A]): F[A] = {
        if (tree == null) result
        else {
          order match {
            case Preorder =>
              val nodeRes = processNode(tree, result)
              val leftRes = dsf(tree.lt, order, nodeRes)
              val rightRes = dsf(tree.rt, order, leftRes)
              rightRes
            case Inorder =>
              val leftRes = dsf(tree.lt, order, result)
              val nodeRes = processNode(tree, leftRes)
              val rightRes = dsf(tree.rt, order, nodeRes)
              rightRes
            case Postorder =>
              val leftRes = dsf(tree.lt, order, result)
              val rightRes = dsf(tree.rt, order, leftRes)
              val nodeRes = processNode(tree, rightRes)
              nodeRes
          }
        }
      }
      dsf(tree, order, emptyContainer.empty)

    }
  }

  implicit class ExtendNodeOps[A: ClassTag](node: Node[A]) extends TreeOps[A] {
    override val aTag: ClassTag[A] = implicitly[ClassTag[A]]
    def listAllNodes[F[_]](order: DsfOrder, listProcess: (Node[A], F[A]) => F[A])(implicit f: MonoidK[F]): F[A] =
      dsfExtendTree[F](node, order, listProcess)
  }


}
