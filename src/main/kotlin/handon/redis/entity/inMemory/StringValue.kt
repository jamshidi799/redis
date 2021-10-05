package handon.redis.entity.inMemory

import java.time.Instant

data class StringValue(
    override val value: String,
    override val expiration: Instant?
) : Value<String>() {

    fun isExpired() = expiration?.isBefore(Instant.now()) ?: false
}

