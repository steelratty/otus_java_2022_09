package ru.otus.TestLog;

public class main {
    public static void main(String[] args) {
        // по сути передаем интерфейс и класс, далее методы класса и интерфейса будут связываться между собой,
        // для переходоа от метода интерфейса к методу класса, который может иметь аннотацию
         TestLoggingInterface myClass =  Ioc.createMyClass(new TestLogging());
         myClass.calculation(1);
         myClass.calculation(2);
         myClass.calculation(2,4);
         myClass.calculation(2,4,6);
    }
}



