package ru.room1408.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.room1408.domain.Message;

public interface DataStore {

    Mono<Message> saveMessage(Message message);

    Flux<Message> loadMessages(String roomId);
}
