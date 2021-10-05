package handon.redis.repository.inMemory

import handon.redis.entity.inMemory.ListValue
import org.springframework.stereotype.Component

@Component
class InMemoryListRepository : InMemoryRepository<ListValue>()
