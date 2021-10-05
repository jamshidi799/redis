package handon.redis.entity

import java.time.Instant

data class ListValue(
    override val value: MutableList<String>,
    override val expiration: Instant?
) : Value<List<String>>()