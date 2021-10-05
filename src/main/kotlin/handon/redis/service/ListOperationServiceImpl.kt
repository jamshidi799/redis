package handon.redis.service

import handon.redis.entity.inMemory.ListValue
import handon.redis.repository.inMemory.InMemoryListRepository
import org.springframework.stereotype.Service

@Service
class ListOperationServiceImpl(
    private val repositoryInMemory: InMemoryListRepository
) : ListOperationService {

    override fun listSet(key: String, index: Int, element: String): String {
        repositoryInMemory.map[key]?.value?.let {
            it[index] = element
            return "OK"
        }
        throw IllegalArgumentException("key $key not found")    // todo: create custom exception type
    }

    override fun listIndex(key: String, index: Int): String {
        repositoryInMemory.map[key]?.value?.let {
            if (index >= 0) return it[index]
            return it[it.size + index]
        }
        throw IllegalArgumentException("key $key not found")
    }

    override fun rightPush(key: String, vararg elements: String): Int {
        repositoryInMemory.map[key]?.value?.let {
            it.addAll(elements)
            return it.size
        }
        repositoryInMemory.map[key] = ListValue(elements.asList().toMutableList(), null)
        return elements.size
    }

    override fun leftPush(key: String, vararg elements: String): Int {
        repositoryInMemory.map[key]?.value?.let {
            it.addAll(0, elements.toList())
            return it.size
        }
        repositoryInMemory.map[key] = ListValue(elements.asList().toMutableList(), null)
        return elements.size
    }

    override fun leftPop(key: String): String {
        repositoryInMemory.map[key]?.value?.let {
            return it.removeFirst()
        }
        throw IllegalArgumentException("key $key not found")
    }

    override fun leftPop(key: String, count: Int): List<String> {
        repositoryInMemory.map[key]?.value?.let {
            val oldList = it.toMutableList()
            it.drop(count)
            return oldList.dropLast(oldList.size - count)
        }
        throw IllegalArgumentException("key $key not found")
    }
}