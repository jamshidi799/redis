package handon.redis.job

import handon.redis.entity.cassandra.StringEntity
import handon.redis.repository.cassandra.CassandraStringRepository
import handon.redis.repository.inMemory.InMemoryStringRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy

@Component
class Snapshot(
    private val inMemoryStringRepository: InMemoryStringRepository,
    private val cassandraStringRepository: CassandraStringRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun snapshot() {
        logger.info("--------- taking snapshot ---------")
        cassandraStringRepository.saveAll(
            inMemoryStringRepository.map
                .asSequence()
                .filter { !it.value.isExpired() }
                .map {
                    StringEntity(it.key, it.value.value, it.value.expiration)
                }.toList()
        )
    }

    @Scheduled(fixedDelay = 60_000L)
    fun schaduler() {
        snapshot()
    }

    @PreDestroy
    fun beforeDestroy() {
        snapshot()
    }
}