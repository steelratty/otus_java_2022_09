package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorDateEx implements Processor {

    public ProcessorDateEx(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        System.out.println(dateTimeProvider.getDate());
        if (dateTimeProvider.getDate().getSecond() % 2  == 0 ){
           throw new Error("************************!!!Приехали!!!************************");
        }
        return message;
    }
    private final DateTimeProvider dateTimeProvider;

}
