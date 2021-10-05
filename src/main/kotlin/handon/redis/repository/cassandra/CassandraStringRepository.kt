package handon.redis.repository.cassandra

import handon.redis.entity.cassandra.StringEntity
import org.springframework.data.cassandra.repository.CassandraRepository


interface CassandraStringRepository : CassandraRepository<StringEntity, Int>