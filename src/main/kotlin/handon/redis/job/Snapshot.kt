package handon.redis.job

import handon.redis.entity.cassandra.ListEntity
import handon.redis.entity.cassandra.StringEntity
import handon.redis.repository.cassandra.CassandraListRepository
import handon.redis.repository.cassandra.CassandraStringRepository
import handon.redis.repository.inMemory.InMemoryListRepository
import handon.redis.repository.inMemory.InMemoryStringRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy

@Component
class Snapshot(
    private val inMemoryStringRepository: InMemoryStringRepository,
    private val cassandraStringRepository: CassandraStringRepository,
    private val inMemoryListRepository: InMemoryListRepository,
    private val cassandraListRepository: CassandraListRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun snapshot() {
        logger.info("--------- taking snapshot ---------")
        saveStrings()
        saveLists()
    }

    @Scheduled(initialDelay = 60_000L, fixedDelay = 60_000L)
    fun schaduler() {
        snapshot()
    }

    @PreDestroy
    fun beforeDestroy() {
        snapshot()
    }

    fun saveStrings() {
        cassandraStringRepository.saveAll(
            inMemoryStringRepository.map
                .filter { !it.value.isExpired() }
                .map {
                    StringEntity(it.key, it.value.value, it.value.expiration)
                }
        )
    }

    fun saveLists() {
        cassandraListRepository.saveAll(
            inMemoryListRepository.map
                .filter { !it.value.isExpired() }
                .map {
                    ListEntity(it.key, it.value.value, it.value.expiration)
                }
        )
    }
}