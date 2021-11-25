package course6.tools.cache.lru

final case class CacheItem[K, V](value: V, left: Option[K], right: Option[K])
