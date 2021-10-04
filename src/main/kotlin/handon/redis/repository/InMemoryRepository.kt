package handon.redis.repository


abstract class InMemoryRepository <T> {
    val map: HashMap<String, T> = hashMapOf()
}