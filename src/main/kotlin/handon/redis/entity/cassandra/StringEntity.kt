package handon.redis.entity.cassandra

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("string")
data class StringEntity(
    @PrimaryKey
    val key: String,
    val value: String,
    val expiration: Instant?
)