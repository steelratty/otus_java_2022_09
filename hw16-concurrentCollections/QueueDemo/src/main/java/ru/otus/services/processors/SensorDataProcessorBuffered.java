package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;


// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private PriorityBlockingQueue<SensorData> queue;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.queue= new  PriorityBlockingQueue(this.bufferSize, Comparator.comparing(o -> ((SensorData) o).getMeasurementTime()));
    }

    @Override
    public void process(SensorData data) {
              if (queue.size() >= bufferSize) {
                  flush();
              }
            queue.put(data);
    }

    public  synchronized  void flush() {
            if (queue.size()==0) return;
            List<SensorData> listBuff = new ArrayList<>();
                try {
                        for (long i = 0; i < bufferSize; i++) {
                            SensorData data = queue.poll();
                            if (data == null) break;
                            listBuff.add(data);
                        }
                       writer.writeBufferedData(listBuff);
                } catch (Exception e) {
                    log.error("Ошибка в процессе записи буфера", e);
                }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
