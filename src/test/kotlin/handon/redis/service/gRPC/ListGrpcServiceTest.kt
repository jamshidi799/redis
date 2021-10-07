package handon.redis.service.gRPC

import handon.redis.repository.inMemory.InMemoryListRepository
import handon.redis.service.ListOperationService
import handon.redis.service.ListOperationServiceImpl
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import redis.list.*

@RunWith(JUnit4::class)
class ListGrpcServiceTest {

    private lateinit var blockingStub: ListServiceGrpc.ListServiceBlockingStub
    private lateinit var listOperationService: ListOperationService

    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    @Before
    fun beforeEach() {
        val repository = InMemoryListRepository()
        listOperationService = ListOperationServiceImpl(repository)
        val serverName = InProcessServerBuilder.generateName()
        grpcCleanup.register(
            InProcessServerBuilder
                .forName(serverName).directExecutor().addService(ListGrpcService(listOperationService)).build()
                .start()
        )
        blockingStub = ListServiceGrpc.newBlockingStub(
            grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
        )
    }

    @Test
    fun listSet() {
        val key = "key"
        val element = "element"
        val newElement = "new element"
        val request = ListSetRequestProto.newBuilder()
            .setKey(key)
            .setElement(newElement)
            .setIndex(0)
            .build()

        listOperationService.rightPush(key, element)
        val response = blockingStub.listSet(request)

        Assertions.assertEquals(
            ListSetResponseProto.newBuilder().setResult("OK").build(),
            response
        )
        Assertions.assertEquals(listOperationService.listIndex(key, 0), newElement)
    }

    @Test
    fun listIndex() {
        val key = "key"
        val element = "element"
        val index = 0
        val request = ListIndexRequestProto.newBuilder()
            .setKey(key)
            .setIndex(index)
            .build()

        listOperationService.rightPush(key, element)
        val response = blockingStub.listIndex(request)

        Assertions.assertEquals(element, response.result)
    }

    @Test
    fun rightPush() {
        val key = "key"
        val elements = listOf("1", "2", "3")
        val request = PushRequestProto.newBuilder()
            .setKey(key)
            .addAllElements(elements)
            .build()

        var response = blockingStub.rightPush(request)
        Assertions.assertEquals(elements.size, response.length)
        response = blockingStub.rightPush(request)
        Assertions.assertEquals(elements.size * 2, response.length)
    }

    @Test
    fun leftPush() {
        val key = "key"
        val elements = listOf("1", "2", "3")
        val request = PushRequestProto.newBuilder()
            .setKey(key)
            .addAllElements(elements)
            .build()

        var response = blockingStub.leftPush(request)
        Assertions.assertEquals(elements.size, response.length)
        response = blockingStub.leftPush(request)
        Assertions.assertEquals(elements.size * 2, response.length)
    }

    @Test
    fun leftPop() {
        val key = "key"
        val elements = listOf("1", "2", "3")
        val request = PopRequestProto.newBuilder()
            .setKey(key)
            .build()

        listOperationService.rightPush(key, *elements.toTypedArray())
        val response = blockingStub.leftPop(request)
        Assertions.assertEquals(elements[0], response.popedValue)
    }

    @Test
    fun multipleLeftPop() {
        val key = "key"
        val elements = listOf("1", "2", "3")
        val request = MultiplePopRequestProto.newBuilder()
            .setKey(key)
            .setCount(elements.size)
            .build()

        listOperationService.rightPush(key, *elements.toTypedArray())
        val response = blockingStub.leftMultiplePop(request)
        Assertions.assertEquals(elements, response.valuesList)
    }
}




















