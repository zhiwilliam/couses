package karrow

object Test extends App {
  import cats.FlatMap
  import cats.implicits._

  final case class Kleisli[F[_], A, B](run: A => F[B]) {
    def compose[Z](k: Kleisli[F, Z, A])(implicit F: FlatMap[F]): Kleisli[F, Z, B] =
      Kleisli[F, Z, B](z => k.run(z).flatMap(run))

    def >>= [Z](k: Kleisli[F, B, Z])(implicit F: FlatMap[F]): Kleisli[F, A, Z] =
      Kleisli[F, A, Z](z => run(z).flatMap(k.run))

    def local[AA](f: AA => A): Kleisli[F, AA, B] = Kleisli(f.andThen(run))
  }

  implicit def kleisliFlatMap[F[_], Z](implicit F: FlatMap[F]): FlatMap[Kleisli[F, Z, *]] =
    new FlatMap[Kleisli[F, Z, *]] {
      def flatMap[A, B](fa: Kleisli[F, Z, A])(f: A => Kleisli[F, Z, B]): Kleisli[F, Z, B] =
        Kleisli(z => fa.run(z).flatMap(a => f(a).run(z)))

      def map[A, B](fa: Kleisli[F, Z, A])(f: A => B): Kleisli[F, Z, B] =
        Kleisli(z => fa.run(z).map(f))

      def tailRecM[A, B](a: A)(f: A => Kleisli[F, Z, Either[A, B]]) =
        Kleisli[F, Z, B]({ z => FlatMap[F].tailRecM(a) { f(_).run(z) } })
    }

  implicit def arrowAs[F[_], A, B](run: A => F[B]) = Kleisli(run)

  val code = ((a: Int) => Option(a + 1)) >>= ((a: Int) => Option(a + 4))
  println(code.run(1))

}
