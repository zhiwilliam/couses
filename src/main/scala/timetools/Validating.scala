package timetools

import cats.data.Validated

trait Validating [A]{
  def validate: Validated[Throwable, A]
}
