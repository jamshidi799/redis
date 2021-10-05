package handon.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class RedisApplication

fun main(args: Array<String>) {
    runApplication<RedisApplication>(*args)
}
