package course5.services

trait StreamReader[A] {
  def read(): fs2.Stream[cats.effect.IO, A]
}
