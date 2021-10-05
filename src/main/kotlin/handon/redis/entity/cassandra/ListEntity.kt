package handon.redis.entity.cassandra

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("list")
data class ListEntity(
    @PrimaryKey
    val key: String,
    val value: List<String>,
    val expiration: Instant?
)