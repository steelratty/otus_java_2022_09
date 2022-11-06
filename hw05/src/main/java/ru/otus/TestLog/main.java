package ru.otus.TestLog;

public class main {
    public static void main(String[] args) {
         TestLoggingInterface myClass =  Ioc.createMyClass();
         myClass.calculation(1);
         myClass.calculation(2);
         myClass.calculation(2,4);
         myClass.calculation(2,4,6);
    }
}



