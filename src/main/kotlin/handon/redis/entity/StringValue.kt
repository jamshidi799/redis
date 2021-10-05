package handon.redis.entity

import java.time.Instant

data class StringValue(
    override val value: String,
    override val expiration: Instant?
): Value<String>()

