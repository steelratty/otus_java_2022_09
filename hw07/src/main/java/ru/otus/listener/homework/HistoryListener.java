package ru.otus.listener.homework;

import com.google.common.collect.Lists;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {
    private     Map<Long,Message> msgHst = new HashMap<>();
    @Override
    public void onUpdated(Message msg) {
        ObjectForMessage n = new ObjectForMessage();
        n.setData(Lists.newArrayList(msg.getField13().getData()));
        msgHst.put(msg.getId(),
        new Message.Builder(msg.getId())
                .field1(msg.getField1())
                .field2(msg.getField2())
                .field3(msg.getField3())
                .field4(msg.getField4())
                .field5(msg.getField5())
                .field6(msg.getField6())
                .field7(msg.getField7())
                .field8(msg.getField8())
                .field9(msg.getField9())
                .field10(msg.getField10())
                .field11(msg.getField11())
                .field12(msg.getField12())
                .field13(n)
                .build()
               ) ;



       // throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(msgHst.get(id));
       // throw new UnsupportedOperationException();
    }
}
