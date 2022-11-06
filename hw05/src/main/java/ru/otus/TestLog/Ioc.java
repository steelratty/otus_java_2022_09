package ru.otus.TestLog;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Executable;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
        }

        /* так как в текущем контексте нам доступны только методы и аннотации интерфейса, мы можем посмотреть
        *  методы класса, сравнить их, при вызове с методами интерфейса и посмотреть аннотацию
        *  недостаток - мы можем только к методам интерфейса обратиться, а не методам класса
        *  и, наверное, нужно лучше arrayList из arrayList, но уже не хочется переписывать
        */
        HashMap<Method, ArrayList<String>> metAnn = new HashMap<>(){
            Class<?> clazz;
            {
                try {
                    clazz = Class.forName(TestLogging.class.getName());
                    Method[] methodsPublic = clazz.getMethods();
                    for (Method method: methodsPublic) {
                        Annotation[] annotations = method.getDeclaredAnnotations();
                        ArrayList<String> strTemp = new ArrayList<>();
                        for (Annotation annotation : annotations) {
                            strTemp.add(annotation.toString());
                        }
                        if (!strTemp.isEmpty())   this.put(method,strTemp);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        // сравнение параметров методов
        boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
            return false;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            for (Method met: metAnn.keySet()) {
                 if (method.getName().equals(met.getName()) // имена совпадают
                     && method.getReturnType() == met.getReturnType() // ретурн тоже
                     &&  equalParamTypes(method.getParameterTypes(),met.getParameterTypes()) // наборы параметров тоже
                 )
                     if (metAnn.get(met).contains("@ru.otus.TestLog.Log()")) {
                         String strParams = "";
                         for (  Object arg :args ){
                             strParams = strParams + " " +arg.toString();
                         }
                         System.out.printf("executed method: %s params:%s ",method,strParams);
                         System.out.println();
                     }
            }

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
