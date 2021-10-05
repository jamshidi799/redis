package handon.redis.entity.inMemory

import java.time.Instant

sealed class Value<T> {
    abstract val value: T
    abstract val expiration: Instant?
}