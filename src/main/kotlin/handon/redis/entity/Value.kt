package handon.redis.entity

import java.time.Instant
import java.util.*

sealed class Value<T> {
    abstract val value: T
    abstract val expiration: Instant?
}