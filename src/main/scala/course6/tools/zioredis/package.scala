package course6.tools

import zio._
import zio.redis._

package object zioredis {
  def putString (key: String, value: String) =
    set(key, value)

  def putInt(key: Int, value: Int) =
    set(key.toString, value.toString)

  def getInt(key: Int) = for {
    value <- get(key.toString)
    intValue <- Task.effect{ value.map(_.toInt).getOrElse(throw new NoSuchElementException) }
  } yield intValue


  val executor = RedisExecutor.live("127.0.0.1", 6379)
}
