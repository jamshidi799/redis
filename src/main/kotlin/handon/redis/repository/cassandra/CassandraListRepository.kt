package handon.redis.repository.cassandra

import handon.redis.entity.cassandra.ListEntity
import org.springframework.data.cassandra.repository.CassandraRepository

interface CassandraListRepository : CassandraRepository<ListEntity, Int>