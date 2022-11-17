package ru.otus.TestLogging;

import java.util.Map;

public interface ATM {
    // так... что умеет атм
    // принимать бабло
    void pushCash(int nominal, int n);
    // выдавать бабло
    Map<Integer,Integer> pullCash(int amount);

    int get_sum();
}
