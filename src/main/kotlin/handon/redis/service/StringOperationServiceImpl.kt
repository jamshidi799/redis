package handon.redis.service

import handon.redis.repository.StringRepository
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class StringOperationServiceImpl(
    private val repository: StringRepository
): StringOperationService {

    override fun set(key: String, value: String, expiration: Duration?) {
        repository.map[key] = value
    }

    override fun get(key: String): String? {
        return repository.map[key]
    }

    override fun getSet(key: String, value: String): String? {
        val oldValue = repository.map[key]
        repository.map[key] = value
        return oldValue
    }

    override fun multipleGet(keys: List<String>) = keys.asIterable()
        .associateBy({it}, { repository.map[it] })
        .mapValues {
            when(it.value){
                is String -> it.value
                else -> null
            }
        }

}