package handon.redis.repository

import handon.redis.entity.StringValue
import org.springframework.stereotype.Repository

@Repository
class StringRepository: InMemoryRepository<StringValue>()
