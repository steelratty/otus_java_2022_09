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

    static TestLoggingInterface createMyClass(Object cl) {
        Class<?>[] iFaces = cl.getClass().getInterfaces();
        InvocationHandler handler = new DemoInvocationHandler(cl, iFaces);
           return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                  iFaces, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object myClass;
        private Map<Method, ArrayList<String>> metAnn = new HashMap<>();
        DemoInvocationHandler(Object myClass, Class<?>[] iFaces) {
            this.myClass = myClass;
            // заполним список методов интерфесов класса с аннотациями класса
            Class<?> clazz;
                try {
                    clazz = Class.forName(this.myClass.getClass().getName());
                    Method[] methods = clazz.getMethods();
                    for (Method method: methods) {
                        Annotation[] annotations = method.getDeclaredAnnotations();
                        ArrayList<String> strTemp = new ArrayList<>();
                        for (Class<?> iCl: iFaces) {
                             for (Method iMet:iCl.getMethods()){
                                 if (equalMethod(iMet,method)) {
                                     for (Annotation annotation : annotations) {
                                         strTemp.add(annotation.toString());
                                     }
                                     if (!strTemp.isEmpty()) metAnn.put (iMet,strTemp);
                                 }
                             }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (metAnn.get(method) != null && metAnn.get(method).contains("@ru.otus.TestLog.Log()")) {
                String strParams = "";
                for (  Object arg :args ){
                    strParams = strParams + " " +arg.toString();
                }
                System.out.printf("executed method: %s params:%s ",method,strParams);
                System.out.println();
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
        // сравнение методов
        private boolean equalMethod(Method met1, Method met2) {
            return met1.getName().equals(met2.getName()) // имена совпадают
                    && met1.getReturnType() == met2.getReturnType() // ретурн тоже
                    &&  equalParamTypes(met1.getParameterTypes(),met2.getParameterTypes()); // наборы параметров тоже
        }

    }
}
