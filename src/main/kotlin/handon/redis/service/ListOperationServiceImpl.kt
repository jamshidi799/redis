package handon.redis.service

import handon.redis.repository.ListRepository

class ListOperationServiceImpl(
    private val repository: ListRepository
) : ListOperationService {

    override fun listSet(key: String, index: Int, element: String): String {
        repository.map[key]?.let {
            if (index >= it.size) throw IllegalArgumentException("index $index is greater than list size ${it.size}")
            it[index] = element
            return "OK"
        }
        throw IllegalArgumentException("key $key not found")
    }

    override fun listIndex(key: String, index: Int): String? {
        repository.map[key]?.let {
            if (index >= it.size) throw IllegalArgumentException("index $index is greater than list size ${it.size}")
            if (index >= 0) return it[index]
            if (it.size + index >= 0) return it[it.size + index]
            throw IllegalArgumentException("index $index is negative")
        }
        throw IllegalArgumentException("key $key not found")
    }

    override fun rightPush(key: String, vararg elements: String): Int {
        repository.map[key]?.let {
            it.addAll(elements)
            return it.size
        }
        repository.map[key] = elements.asList().toMutableList()
        return elements.size
    }

    override fun leftPush(key: String, vararg elements: String): Int {
        repository.map[key]?.let {
            it.addAll(0, elements.toList())
            return it.size
        }
        repository.map[key] = elements.asList().toMutableList()
        return elements.size
    }

    override fun leftPop(key: String): String? {
        repository.map[key]?.let {
            return it.removeFirst()
        }
        throw IllegalArgumentException("key $key not found")
    }

    override fun leftPop(key: String, count: Int): List<String> {
        repository.map[key]?.let {
            return it.drop(count)
        }
        throw IllegalArgumentException("key $key not found")
    }
}