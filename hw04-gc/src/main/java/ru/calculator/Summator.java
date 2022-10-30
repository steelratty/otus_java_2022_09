package ru.calculator;

import java.util.ArrayList;
import java.util.List;

public class Summator {
    private long sum = 0;
    private long prevValue = 0;
    private long prevPrevValue = 0;
    private long sumLastThreeValues = 0;
    private long someValue = 0;
    private final List<Data> listValues = new ArrayList<>();

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        if (listValues.size() % 6_600_000 == 0) {
            listValues.clear();
        }
        sum += data.getValue();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
            someValue = Math.abs(someValue) + listValues.size();
        }
    }

    public long getSum() {
        return sum;
    }

    public long getPrevValue() {
        return prevValue;
    }

    public long getPrevPrevValue() {
        return prevPrevValue;
    }

    public long getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public long getSomeValue() {
        return someValue;
    }
}
