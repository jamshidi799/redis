package handon.redis.repository.inMemory

import handon.redis.entity.inMemory.StringValue
import org.springframework.stereotype.Component

@Component
class InMemoryStringRepository : InMemoryRepository<StringValue>()
