package handon.redis.repository.inMemory


abstract class InMemoryRepository <T> {
    val map: HashMap<String, T> = hashMapOf()
}