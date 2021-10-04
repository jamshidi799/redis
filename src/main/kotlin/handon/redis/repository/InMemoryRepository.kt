package handon.redis.repository

import org.springframework.stereotype.Repository


abstract class InMemoryRepository <T> {
    val map: HashMap<String, T> = hashMapOf()
}

@Repository
class StringRepository: InMemoryRepository<String>()

@Repository
class ListRepository: InMemoryRepository<MutableList<String>>()
