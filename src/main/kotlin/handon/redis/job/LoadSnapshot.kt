package handon.redis.job

import handon.redis.entity.inMemory.ListValue
import handon.redis.entity.inMemory.StringValue
import handon.redis.repository.cassandra.CassandraListRepository
import handon.redis.repository.cassandra.CassandraStringRepository
import handon.redis.repository.inMemory.InMemoryListRepository
import handon.redis.repository.inMemory.InMemoryStringRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LoadSnapshot(
    private val inMemoryStringRepository: InMemoryStringRepository,
    private val cassandraStringRepository: CassandraStringRepository,
    private val inMemoryListRepository: InMemoryListRepository,
    private val cassandraListRepository: CassandraListRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener(ApplicationReadyEvent::class)
    fun load() {
        logger.info("--------- load started ---------")

        cassandraStringRepository.findAll().forEach {
            inMemoryStringRepository.map[it.key] = StringValue(it.value, it.expiration)
        }

        cassandraListRepository.findAll().forEach {
            inMemoryListRepository.map[it.key] = ListValue(it.value.toMutableList(), it.expiration)
        }
    }
}