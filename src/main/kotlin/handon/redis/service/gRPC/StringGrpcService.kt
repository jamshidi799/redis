package handon.redis.service.gRPC

import handon.redis.service.StringOperationService
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import redis.string.*
import java.time.Duration

@GrpcService
class StringGrpcService(
    private val stringOperationService: StringOperationService
) : StringServiceGrpc.StringServiceImplBase() {

    override fun set(request: SetRequestProto, responseObserver: StreamObserver<EmptyResponseProto>) {
        stringOperationService.set(
            request.key,
            request.value,
            Duration.ofSeconds(request.expiration.seconds, request.expiration.nanos.toLong())
        )
        responseObserver.onNext(EmptyResponseProto.getDefaultInstance());
        responseObserver.onCompleted();
    }

    override fun get(request: GetRequestProto, responseObserver: StreamObserver<GetResponseProto>) {
        val value = stringOperationService.get(request.key)
        responseObserver.onNext(GetResponseProto.newBuilder().setValue(value ?: "").build());
        responseObserver.onCompleted();
    }

    override fun getSet(request: GetSetRequestProto, responseObserver: StreamObserver<GetResponseProto>) {
        val value = stringOperationService.getSet(request.key, request.value)
        responseObserver.onNext(GetResponseProto.newBuilder().setValue(value ?: "").build());
        responseObserver.onCompleted();
    }

    override fun multipleGet(
        request: MultipleGetRequestProto,
        responseObserver: StreamObserver<MultipleGetResponseProto>
    ) {
        val values = stringOperationService.multipleGet(request.keysList).mapValues { it.value ?: "" }
        responseObserver.onNext(MultipleGetResponseProto.newBuilder().putAllValues(values).build());
        responseObserver.onCompleted();
    }
}