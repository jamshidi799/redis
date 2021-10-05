package handon.redis.repository

import handon.redis.entity.ListValue
import org.springframework.stereotype.Repository

@Repository
class ListRepository: InMemoryRepository<ListValue>()
