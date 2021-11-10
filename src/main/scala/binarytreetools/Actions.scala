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
    protected def dsfTraverse[F[_], B](tree: Node[A], order: DsfOrder, processNode: (Node[A], F[B]) => F[B])
                                      (implicit emptyContainer: MonoidK[F]): F[B] = {
      def dsf(tree: Node[A], order: DsfOrder, result: F[B]): F[B] = {
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


    protected def dsfPostOrderSearch[F[_], B](tree: Node[A], processNode: (Node[A], F[B], F[B]) => F[B])
                                             (implicit emptyContainer: MonoidK[F]): F[B] = {
      def searchChild(tree: Node[A]): F[B] = {
        if (tree == null) emptyContainer.empty
        else {
          val leftRes = searchChild(tree.lt)
          val rightRes = searchChild(tree.rt)
          val nodeRes = processNode(tree, leftRes, rightRes)
          nodeRes
        }
      }
      searchChild(tree)
    }
  }

  implicit class ExtendNodeOps[A: ClassTag](tree: Node[A]) extends TreeOps[A] {
    override val aTag: ClassTag[A] = implicitly[ClassTag[A]]
    def dsfTraverse[F[_], B](order: DsfOrder, listProcess: (Node[A], F[B]) => F[B])(implicit f: MonoidK[F]): F[B] =
      dsfTraverse[F, B](tree, order, listProcess)

    def dsfPostOrderSearch[F[_], B](listProcess: (Node[A], F[B], F[B]) => F[B])(implicit f: MonoidK[F]): F[B] =
      dsfPostOrderSearch[F, B](tree, listProcess)

    def prettyPrint(): Unit = {
      dsfPostOrderSearch[Vector, String]( tree,
        (node: Node[A], leftResult: Vector[String], rightResult: Vector[String]) => {
          if (leftResult.isEmpty && rightResult.isEmpty) {
            Vector(node.value +  " ")
          }
          else {
            def fillLines(lineNumber: Int, lineLength: Int): Vector[String] = {
              val len = if(lineLength == 0) 1 else lineLength
              val emptyLine = Vector.fill(len)(' ').mkString
              Vector.fill(lineNumber)(emptyLine)
            }


            val leftLength = leftResult.headOption.map(_.length).getOrElse(0)
            val rightLength = rightResult.headOption.map(_.length).getOrElse(0)

            val left: Vector[String] = if (leftResult.length < rightResult.length) leftResult ++ fillLines(rightResult.length - leftResult.length, leftLength) else leftResult
            val right = if (rightResult.length < leftResult.length) rightResult ++ fillLines(leftResult.length - rightResult.length, rightLength) else rightResult

            def slashAt(value: String): Int = {
              val startAt = value.indexWhere(_ != ' ')
              val endAt = value.indexWhere(_ == ' ', startAt)
              startAt + (endAt - startAt) / 2
            }

            val nodeLength = node.value.toString.length
            val nodeStartAt = {
              val res = leftLength - nodeLength / 2 - 1
              if(res > 0) res else 0
            }
            val nodeEndAt = nodeStartAt + nodeLength

            val (leftSlashAt, leftSlashLine) = if(leftLength > 0) {
              val leftSlashAt = slashAt(left.head)
              val leftSlashLine =
                ((Vector.fill(leftSlashAt)(' ') :+ '/') ++ Vector.fill(leftLength - leftSlashAt -1 )(' ')).mkString //
              (leftSlashAt, leftSlashLine)
            } else (-1,  " ")

            val(rightSlashAt, rightSlashLine) = if(rightLength > 0) {
              val rightSlashAt = slashAt(right.head)
              val rightSlashLine =
                ((Vector.fill(rightSlashAt)(' ') :+ '\\') ++ Vector.fill(rightLength - rightSlashAt - 1)(' ')).mkString //
              (rightSlashAt, rightSlashLine)
            } else (-1,  " ")


            //val totalLength = leftLength + rightLength
            val nodeLineLeft: String = if(leftSlashAt >= 0) {
              (Vector.fill(leftSlashAt + 1)(' ') ++ Vector.fill(nodeStartAt - leftSlashAt - 1)('_')).mkString
            } else ""
            val nodeLineRight: String  = if(rightSlashAt >= 0) {
              (Vector.fill(leftLength + rightSlashAt - nodeEndAt)('_') ++ Vector.fill(rightLength - rightSlashAt)(' ')).mkString
            } else ""

            val rightEmptyInsert = if(rightLength == 0)  " " else ""
            val nodeLine = nodeLineLeft + node.value.toString + rightEmptyInsert + nodeLineRight

            val result = ((Vector(leftSlashLine) ++ left) zip (Vector(rightSlashLine) ++ right)).map{x =>
              x._1 + x._2
            }
            Vector(nodeLine) ++ result
          }
        }).foreach(println)
    }
  }


}
