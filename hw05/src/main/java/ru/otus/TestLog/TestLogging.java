package ru.otus.TestLog;


public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param) {
        System.out.println("!!calculation, param:" + param);
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.printf("!!calculation, param1: %s param2: %s", param1,param2 );
        System.out.println();
    }
    @Log
    @Override
    public void calculation(int param1, int param2, int param3) {
        System.out.printf("!!calculation, param1: %s, param2: %s, param3: %s", param1, param2, param3 );
        System.out.println();
    }
}
