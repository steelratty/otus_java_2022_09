package ru.otus.homework;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RunTests {
    public static void executeTest(String strTest) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
       // получить класс из строки, создать объект
        Class<?> clazz = Class.forName(strTest);
        List<String> tests_success = new ArrayList<>();
        List<String> tests_fail = new ArrayList<>();
       //ищем все методы с аннотацией test
        Method[] methodsPublic = clazz.getMethods();
        for (Method method: methodsPublic) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.toString().contains("Test()")) {
                    // создаем для пачки тестов
                    if (runTest(clazz, method))
                        tests_success.add(method.toString());
                    else
                        tests_fail.add(method.toString());
                }
            }
        }
        // выведдем результат
        System.out.println("=======================================");
        System.out.println("RESULT:");

        System.out.printf("Successed - %d (",tests_success.size());
        tests_success.forEach(o-> System.out.println(o+" "));
        System.out.println(")");

        System.out.printf("Failed - %d (",tests_fail.size());
        tests_fail.forEach(o-> System.out.println(o+" "));
        System.out.println(")");


    }

    private static boolean runTest(Class<?> clazz, Method testMethod) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        boolean ret = true;
        boolean rettest =true;
        // создаем новый тестировочный объект
        Constructor constructor = clazz.getConstructor();
        Object o =  constructor.newInstance();

        rettest = runBeforeAfter(clazz,o,"Before()");

        // выполняем тест только когда отработал before
        if (rettest) {
            try {
                testMethod.invoke(o);
            } catch (Exception E) {
                rettest = false;
            }
        }

        // в любом случае выполняем After
        ret = runBeforeAfter(clazz,o,"After()");
        return ret && rettest; // тест провален или прошел, но after отпал
    }

    private static boolean runBeforeAfter( Class<?> clazz, Object o, String anNote){
        Method[] methodsPublic = clazz.getMethods();
        boolean ret = true;
        for (Method method: methodsPublic) {
            Annotation[] annotations = method.getDeclaredAnnotations();{
                for (Annotation annotation: annotations){
                    if (annotation.toString().contains(anNote)){
                        try {
                            method.invoke(o);
                        }
                        catch (Exception E) {
                            ret = false; // ошибка при вызове
                        }
                    }
                }
            }
        }
        return ret;
    }


}
