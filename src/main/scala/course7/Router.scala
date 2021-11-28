package course7

import cats.Alternative
import cats.implicits._

trait Router[-IN, +OUT] {
  def process(in: IN): Either[_, OUT]
}

trait RouteTo[I] {
  def to[O1, O2>:O1](f: I => O1): Router[I, O2]
}

trait RouterMaker[I] {
  def route(condition: I => Boolean): RouteTo[I] = new RouteTo[I] {
    override def to[O1, O2 >: O1](f: I => O1): Router[I, O2] =
      (in: I) =>
        if (condition(in))
          Right(f(in))
        else
          Left(new Throwable("Not fit router found"))
  }

  type NewRouter[T] = Router[I, T]
  implicit val routeAlternative = new Alternative[NewRouter] {
    override def empty[A]: NewRouter[A] = (_: I) => Left(new Throwable("Not fit"))

    override def combineK[A](x: NewRouter[A], y: NewRouter[A]): NewRouter[A] =
      (in: I) => x.process(in).orElse(y.process(in))

    override def pure[A](x: A): NewRouter[A] = route(_ => true) to (_ => x)

    override def ap[A, B](ff: NewRouter[A => B])(fa: NewRouter[A]): NewRouter[B] =
      (in: I) => fa.process(in) ap ff.process(in)
  }

}
