package handon.redis.service

import handon.redis.repository.ListRepository
import org.springframework.stereotype.Service

@Service
class ListOperationServiceImpl(
    private val repository: ListRepository
) : ListOperationService {

    override fun listSet(key: String, index: Int, element: String): String {
        repository.map[key]?.let {
            it[index] = element
            return "OK"
        }
        throw IllegalArgumentException("key $key not found")    // todo: create custom exception type
    }

    override fun listIndex(key: String, index: Int): String {
        repository.map[key]?.let {
            if (index >= 0) return it[index]
            return it[it.size + index]
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

    override fun leftPop(key: String): String {
        repository.map[key]?.let {
            return it.removeFirst()
        }
        throw IllegalArgumentException("key $key not found")
    }

    override fun leftPop(key: String, count: Int): List<String> {
        repository.map[key]?.let {
            val oldList = it.toMutableList()
            it.drop(count)
            return oldList.dropLast(oldList.size - count)
        }
        throw IllegalArgumentException("key $key not found")
    }
}