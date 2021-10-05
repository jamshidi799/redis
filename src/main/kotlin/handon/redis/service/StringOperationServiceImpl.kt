package handon.redis.service

import handon.redis.entity.inMemory.StringValue
import handon.redis.repository.inMemory.InMemoryStringRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class StringOperationServiceImpl(
    private val repositoryInMemory: InMemoryStringRepository
) : StringOperationService {

    override fun set(key: String, value: String, expiration: Duration?) {
        val instant = expiration?.let { Instant.now().plus(expiration) }
        repositoryInMemory.map[key] = StringValue(value, instant)
    }

    override fun get(key: String): String? {
        return repositoryInMemory.map[key]?.let { value -> extractValue(key, value) }
    }

    override fun getSet(key: String, value: String): String? {
        val oldValue = repositoryInMemory.map[key]
        repositoryInMemory.map[key] = StringValue(value, oldValue?.expiration)
        return oldValue?.value
    }

    override fun multipleGet(keys: List<String>) = keys.asIterable()
        .associateBy({ it }, { repositoryInMemory.map[it] })
        .mapValues {
            it.value?.let { value -> extractValue(it.key, value) }
        }

    fun extractValue(key: String, stringValue: StringValue): String? {
        if (!stringValue.isExpired()) return stringValue.value
        repositoryInMemory.map.remove(key)
        return null
    }

}