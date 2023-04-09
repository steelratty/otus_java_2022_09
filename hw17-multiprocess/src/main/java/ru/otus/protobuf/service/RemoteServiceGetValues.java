package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.*;

public class RemoteServiceGetValues extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void getValues(UFirstNextValues request, StreamObserver<UNextValue> responseObserver)  {
        for (long i = request.getFirstValue(); i<= request.getLastValue(); i++ ){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            e.printStackTrace();
            }
            responseObserver.onNext(UNextValue.newBuilder().setValue(i).build());
        }
        responseObserver.onCompleted();
    }

}
