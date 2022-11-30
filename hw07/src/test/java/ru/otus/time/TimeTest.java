package ru.otus.time;


import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TimeTest {
    class localDt1Sec implements DateTimeProvider {
        @Override
        public LocalDateTime getDate() {
            return LocalDateTime.now().withSecond(1);
        }
    }
    class localDt2Sec implements DateTimeProvider {
        @Override
        public LocalDateTime getDate() {
            return LocalDateTime.now().withSecond(22);
        }
    }

    @Test
    void timeTest1Sec() {

        var processors = List.of((Processor) new ProcessorDateEx(new localDt1Sec()));
        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        complexProcessor.removeListener(listenerPrinter);
    }

    @Test
    void timeTest2Sec() {
        var processors = List.of((Processor) new ProcessorDateEx(new localDt2Sec()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();
        boolean isErr = false;
        Message result = null;

        try {
             result = complexProcessor.handle(message);
        }
        catch (Error e){
            System.out.println("OK");
            isErr = true;
        }

        if (!isErr){
            throw new Error("Должно было вызвать ошибку, но не вызвало");
        }
        complexProcessor.removeListener(listenerPrinter);
    }
}