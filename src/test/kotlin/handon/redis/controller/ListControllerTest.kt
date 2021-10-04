package handon.redis.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import handon.redis.controller.request.ListPushRequest
import handon.redis.controller.request.ListSetRequest
import handon.redis.controller.request.SetRequest
import handon.redis.service.ListOperationService
import handon.redis.service.StringOperationService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(ListController::class)
internal class ListControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var service: ListOperationService

    @Test
    fun listSet() {
        val key = "key"
        val index = 0
        val element = "val"
        val body = ListSetRequest(key, index, element)

        mockMvc.post("/list/lSet") {
            content = jacksonObjectMapper().writeValueAsString(body)
            contentType = MediaType.APPLICATION_JSON
        }

        Mockito.verify(service).listSet(key, index, element)
    }

    @Test
    fun listIndex() {
        val key = "key"
        val index = 0
        mockMvc.get("/list/lIndex?key=$key&index=$index") {}

        Mockito.verify(service).listIndex(key, index)
    }

    @Test
    fun rightPush() {
        val key = "key"
        val elements = listOf("val")
        val body = ListPushRequest(elements)

        mockMvc.post("/list/rPush/$key") {
            content = jacksonObjectMapper().writeValueAsString(body)
            contentType = MediaType.APPLICATION_JSON
        }

        Mockito.verify(service).rightPush(key, *elements.toTypedArray())
    }

    @Test
    fun leftPush() {
        val key = "key"
        val elements = listOf("val")
        val body = ListPushRequest(elements)

        mockMvc.post("/list/lPush/$key") {
            content = jacksonObjectMapper().writeValueAsString(body)
            contentType = MediaType.APPLICATION_JSON
        }

        Mockito.verify(service).leftPush(key, *elements.toTypedArray())
    }

    @Test
    fun leftPop() {
        val key = "key"

        mockMvc.delete("/list/lPop/$key") { }

        Mockito.verify(service).leftPop(key)
    }

    @Test
    fun leftPopWithCount() {
    }
}