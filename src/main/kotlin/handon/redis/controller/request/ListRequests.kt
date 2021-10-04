package handon.redis.controller.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Duration

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListSetRequest(
    val key: String,
    val index: Int,
    val element: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListPushRequest(
    val elements: List<String>,
)