package handon.redis.service

import handon.redis.entity.StringValue
import handon.redis.repository.StringRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class StringOperationServiceImpl(
    private val repository: StringRepository
) : StringOperationService {

    override fun set(key: String, value: String, expiration: Duration?) {
        val instant = expiration?.let { Instant.now().plus(expiration) }
        repository.map[key] = StringValue(value, instant)
    }

    override fun get(key: String): String? {
        return repository.map[key]?.let { value -> extractValue(key, value) }
    }

    override fun getSet(key: String, value: String): String? {
        val oldValue = repository.map[key]
        repository.map[key] = StringValue(value, oldValue?.expiration)
        return oldValue?.value
    }

    override fun multipleGet(keys: List<String>) = keys.asIterable()
        .associateBy({ it }, { repository.map[it] })
        .mapValues {
            it.value?.let { value -> extractValue(it.key, value) }
        }

    fun extractValue(key: String, stringValue: StringValue): String? {
        if (!isExpired(stringValue.expiration)) return stringValue.value
        repository.map.remove(key)
        return null
    }

    fun isExpired(expiration: Instant?): Boolean = expiration?.isBefore(Instant.now()) ?: false

}