package handon.redis.service.gRPC

import handon.redis.service.ListOperationService
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import redis.list.*

@GrpcService
class ListGrpcService(
    private val listOperationService: ListOperationService
) : ListServiceGrpc.ListServiceImplBase() {

    override fun listSet(request: ListSetRequestProto, responseObserver: StreamObserver<ListSetResponseProto>) {
        val result = listOperationService.listSet(request.key, request.index, request.element)
        responseObserver.onNext(ListSetResponseProto.newBuilder().setResult(result).build())
        responseObserver.onCompleted()
    }

    override fun listIndex(request: ListIndexRequestProto, responseObserver: StreamObserver<ListIndexResponseProto>) {
        val element = listOperationService.listIndex(request.key, request.index) ?: ""
        responseObserver.onNext(ListIndexResponseProto.newBuilder().setResult(element).build())
        responseObserver.onCompleted()
    }

    override fun rightPush(request: PushRequestProto, responseObserver: StreamObserver<PushResponseProto>) {
        val listLength = listOperationService.rightPush(request.key, *request.elementsList.toTypedArray())
        responseObserver.onNext(PushResponseProto.newBuilder().setLength(listLength).build())
        responseObserver.onCompleted()
    }

    override fun leftPush(request: PushRequestProto, responseObserver: StreamObserver<PushResponseProto>) {
        val listLength = listOperationService.leftPush(request.key, *request.elementsList.toTypedArray())
        responseObserver.onNext(PushResponseProto.newBuilder().setLength(listLength).build())
        responseObserver.onCompleted()
    }

    override fun leftPop(request: PopRequestProto, responseObserver: StreamObserver<PopResponseProto>) {
        val popedValue = listOperationService.leftPop(request.key) ?: ""
        responseObserver.onNext(PopResponseProto.newBuilder().setPopedValue(popedValue).build())
        responseObserver.onCompleted()
    }

    override fun leftMultiplePop(
        request: MultiplePopRequestProto,
        responseObserver: StreamObserver<MultiplePopResponseProto>
    ) {
        val poppedValues = listOperationService.leftPop(request.key, request.count)
        responseObserver.onNext(MultiplePopResponseProto.newBuilder().addAllValues(poppedValues).build())
        responseObserver.onCompleted()
    }
}