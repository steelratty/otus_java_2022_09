package ru.otus.homework;

public class TestClass {
    @Before
    public TestClass before1() {
        System.out.println("test_running - before 1");
        return this;
    }
    @Before
    public TestClass before2() {
        System.out.println("test_running -before 2");
        return this;
    }
    @Before
    public TestClass before3() {
        System.out.println("test_running -before 3");
        return this;
    }
    @After
    public TestClass after1() {
        System.out.println("test_running -after 1");
        return this;
    }
    @Test
    public TestClass test1() {
        System.out.println("test_running -test 1");
        return this;
    }
    @Test
    public TestClass test2() {
        System.out.println("test_running -test 2");
        return this;
    }
    @Test
    public TestClass test3() {
        System.out.println("test_running -test 3");
        return this;
    }


}
