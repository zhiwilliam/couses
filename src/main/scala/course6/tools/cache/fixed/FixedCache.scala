package course6.tools.cache.fixed

import zio._

final class FixedCache [K, V] private (val items: Map[K, V]) { self =>
  def get(key: K): ZIO[Any, NoSuchElementException, V] = for {
      item <- ZIO.fromOption(items.get(key)).orElseFail(new NoSuchElementException(s"Key does not exist: $key"))
    } yield item
}

object FixedCache {
  def make[K, V](items: Map[K, V]): IO[IllegalArgumentException, FixedCache[K, V]] = {
    if(items != null)
      ZIO.succeed(new FixedCache[K, V](items))
    else {
      ZIO.fail(new IllegalArgumentException("Initial Items cannot be null!"))
    }
  }
}