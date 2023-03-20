package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import org.checkerframework.checker.units.qual.A;
import ru.otus.protobuf.generated.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteDBServiceGrpc.newBlockingStub(channel);
        var getValueMsgIterator =
                stub.getValues(
                UFirstNextValues.newBuilder().setFirstValue(1).setLastValue(30).build());
        logger.info("numbers Client is starting...");
        AtomicLong currServVal = new AtomicLong();
        AtomicBoolean isNew = new AtomicBoolean(); // null;
        Thread th1 = new Thread(() -> {
            getValueMsgIterator.forEachRemaining(um ->
                    {
                            currServVal.set(um.getValue());
                            isNew.set(true);
                            logger.info(String.format("new_value: %d", currServVal.longValue()));
                    }
            );
            Thread.currentThread().interrupt();
        });
        th1.start();

        long currentValue = 0;
        while (!th1.isInterrupted() ){
            Thread.sleep(1000);
                currentValue = currentValue + (isNew.get()?currServVal.longValue():0 )+ 1;
                isNew.set(false);
                logger.info(String.format("current_value: %d", currentValue));
        }

        channel.shutdown();

    }
}
