package handon.redis.repository

import org.springframework.stereotype.Repository

@Repository
class StringRepository: InMemoryRepository<String>()
