package ru.otus.TestLog;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createMyClass(Object cl, Class<?>[] iFace) {
           InvocationHandler handler = new DemoInvocationHandler(cl);
           return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                  iFace, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object myClass;
        private Map<Method, ArrayList<String>> metAnn = new HashMap<>();
        DemoInvocationHandler(Object myClass) {
            this.myClass = myClass;
            // заполним список методов класса с аннотациями
            Class<?> clazz;
                try {
                    clazz = Class.forName(this.myClass.getClass().getName());
                    Method[] methodsPublic = clazz.getMethods();
                    for (Method method: methodsPublic) {
                        Annotation[] annotations = method.getDeclaredAnnotations();
                        ArrayList<String> strTemp = new ArrayList<>();
                        for (Annotation annotation : annotations) {
                            strTemp.add(annotation.toString());
                        }
                        if (!strTemp.isEmpty()) metAnn.put (method,strTemp);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
        // сравнение параметров методов
        private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
            return false;
        }
    }
}
