package course6.tools.cache.fixed

import zio.{IO, UIO, ZIO}
import zio.stm.{STM, TMap}


final class ConcurrentFixedCache[K, V] private (val items: TMap[K, V]) { self =>
  def get(key: K): ZIO[Any, NoSuchElementException, V] =
    (for {
      optionItem <- self.items.get(key)
      item       <- STM.fromOption(optionItem).mapError(_ => new NoSuchElementException(s"Key does not exist: $key"))
    } yield item).commitEither.refineToOrDie[NoSuchElementException]

  def put(key: K, value: V): UIO[Unit] =
    (for {
      _  <- STM.ifM(self.items.contains(key))(updateItem(key, value), addNewItem(key, value))
    } yield ()).commitEither.orDie

  private def updateItem(key: K, value: V): STM[Error, Unit] = self.items.put(key, value)

  private def addNewItem(key: K, value: V): STM[Error, Unit] = self.items.put(key, value)
}

object ConcurrentFixedCache {
  def make[K, V](items: Map[K, V]): IO[IllegalArgumentException, ConcurrentFixedCache[K, V]] = {
    if(items != null)
      (TMap.fromIterable(items).map(itemsRef => new ConcurrentFixedCache[K, V](itemsRef))).commit
    else {
      ZIO.fail(new IllegalArgumentException("Initial Items cannot be null!"))
    }
  }
}