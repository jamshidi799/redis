package handon.redis.repository

import org.springframework.stereotype.Repository

@Repository
class ListRepository: InMemoryRepository<MutableList<String>>()
