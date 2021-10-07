package handon.redis.service.gRPC

import com.google.protobuf.Duration
import handon.redis.repository.inMemory.InMemoryStringRepository
import handon.redis.service.StringOperationService
import handon.redis.service.StringOperationServiceImpl
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import redis.string.*


@RunWith(JUnit4::class)
class StringGrpcServiceTest {

    private lateinit var blockingStub: StringServiceGrpc.StringServiceBlockingStub
    private lateinit var stringOperationService: StringOperationService

    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    @Before
    fun beforeEach() {
        val repository = InMemoryStringRepository()
        stringOperationService = StringOperationServiceImpl(repository)
        val serverName = InProcessServerBuilder.generateName()
        grpcCleanup.register(
            InProcessServerBuilder
                .forName(serverName).directExecutor().addService(StringGrpcService(stringOperationService)).build()
                .start()
        )
        blockingStub = StringServiceGrpc.newBlockingStub(
            grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
        )
    }

    @Test
    fun set() {
        val request = SetRequestProto.newBuilder()
            .setKey("key")
            .setValue("value")
            .setExpiration(Duration.newBuilder().setSeconds(1000))
            .build()

        val reply = blockingStub.set(request)
        assertEquals(EmptyResponseProto.getDefaultInstance(), reply)
        assertEquals(stringOperationService.get("key"), "value")
    }

    @Test
    fun get() {
        val key = "key"
        val value = "value"
        val setRequest = SetRequestProto.newBuilder()
            .setKey(key)
            .setValue(value)
            .setExpiration(Duration.newBuilder().setSeconds(1000))
            .build()
        val getRequest = GetRequestProto.newBuilder()
            .setKey(key)
            .build()

        blockingStub.set(setRequest)
        val actual = blockingStub.get(getRequest).value
        assertEquals(value, actual)
    }

    @Test
    fun getSet() {
        val key = "key"
        val value = "value"
        val newValue = "newValue"
        val setRequest = SetRequestProto.newBuilder()
            .setKey(key)
            .setValue(value)
            .setExpiration(Duration.newBuilder().setSeconds(1000))
            .build()
        val getSetRequest = GetSetRequestProto.newBuilder()
            .setKey(key)
            .setValue(newValue)
            .build()

        blockingStub.set(setRequest)
        val actual = blockingStub.getSet(getSetRequest).value
        assertEquals(value, actual)
    }

    @Test
    fun multipleGet() {
        val expectedMap: MutableMap<String, String> = mutableMapOf(
            "key1" to "value1",
            "key2" to "value2"
        )
        val multipleGetResponse = MultipleGetRequestProto.newBuilder()
            .addAllKeys(expectedMap.keys)
            .build()

        expectedMap.forEach { stringOperationService.set(it.key, it.value, null) }
        val actual = blockingStub.multipleGet(multipleGetResponse).valuesMap
        assertEquals(expectedMap, actual)
    }
}