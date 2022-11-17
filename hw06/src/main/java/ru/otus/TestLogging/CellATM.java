package ru.otus.TestLogging;

public interface CellATM {
    // что может делать ячейка:
    // положить в себя наличные в количестве, штук
    void push(int n);
    // выдать наличные
    void pull(int n);
    //сказать сколько купюр имеется
    int getN();
    // сказать емкость ячейки
    int getMaxN();
    // сказать свой номинал
    int getNominal();
}
