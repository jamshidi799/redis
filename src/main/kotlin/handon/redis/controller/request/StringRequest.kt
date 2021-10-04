package handon.redis.controller.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Duration

@JsonIgnoreProperties(ignoreUnknown = true)
data class SetRequest(
    val key: String,
    val value: String,
    val expiration: Duration? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MGetRequest(
    val keys: List<String>
)