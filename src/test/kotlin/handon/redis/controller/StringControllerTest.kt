package handon.redis.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import handon.redis.RedisApplication
import handon.redis.controller.request.MGetRequest
import handon.redis.controller.request.SetRequest
import handon.redis.service.StringOperationService
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@WebMvcTest(StringController::class)
internal class StringControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var service: StringOperationService

    @Test
    fun set() {
        val key = "key"
        val value = "val"
        val body = SetRequest(key, value)

        mockMvc.post("/string/set") {
            content = jacksonObjectMapper().writeValueAsString(body)
            contentType = MediaType.APPLICATION_JSON
        }

        verify(service).set(key, value, null)
    }

    @Test
    fun get() {
        val key = "key"
        mockMvc.get("/string/get/$key"){}

        verify(service).get(key)
    }

    @Test
    fun getSet() {
        val key = "key"
        val value = "val"
        val body = SetRequest(key, value)
        mockMvc.post("/string/get-set"){
            content = jacksonObjectMapper().writeValueAsString(body)
            contentType = MediaType.APPLICATION_JSON
        }

        verify(service).getSet(key, value)
    }

    @Test
    fun mGet() {
        val keys = listOf("key")
        val body = MGetRequest(keys)
        mockMvc.get("/string/m-get"){
            content = jacksonObjectMapper().writeValueAsString(body)
            contentType = MediaType.APPLICATION_JSON
        }

        verify(service).multipleGet(keys)
    }
}