package handon.redis.service.gRPC

import com.google.protobuf.Duration
import handon.redis.repository.inMemory.InMemoryStringRepository
import handon.redis.service.StringOperationService
import handon.redis.service.StringOperationServiceImpl
import io.grpc.internal.testing.StreamRecorder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import redis.string.EmptyResponseProto
import redis.string.GetRequestProto
import redis.string.GetResponseProto
import redis.string.SetRequestProto
import java.util.concurrent.TimeUnit

internal class StringGrpcServiceNotGracefulTest {

    private lateinit var underTest: StringGrpcService
    private lateinit var stringOperationService: StringOperationService

    @BeforeEach
    fun beforeEach() {
        val repository = InMemoryStringRepository()
        stringOperationService = StringOperationServiceImpl(repository)
        underTest = StringGrpcService(stringOperationService)
    }

    @Test
    fun set() {
        val key = "key"
        val value = "value"
        val request = SetRequestProto.newBuilder()
            .setKey(key)
            .setValue(value)
            .setExpiration(Duration.newBuilder().setSeconds(1000))
            .build()

        val responseObserver: StreamRecorder<EmptyResponseProto> = StreamRecorder.create()

        underTest.set(request, responseObserver)
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time")
        }
        assertNull(responseObserver.error)
        val results = responseObserver.values

        assertEquals(1, results.size)
        val response = results[0]
        assertEquals(
            EmptyResponseProto.getDefaultInstance(), response
        )
        assertEquals(stringOperationService.get(key), value)
    }

    @Test
    fun get() {
        val key = "key"
        val value = "value"
        val request = GetRequestProto.newBuilder()
            .setKey(key)
            .build()

        val responseObserver: StreamRecorder<GetResponseProto> = StreamRecorder.create()

        stringOperationService.set(key, value, null)
        underTest.get(request, responseObserver)
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time")
        }
        assertNull(responseObserver.error)
        val results = responseObserver.values

        assertEquals(1, results.size)
        val response = results[0]
        assertEquals(
            GetResponseProto.newBuilder().setValue(value).build(), response
        )
    }
}




