package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    record MarkedMethod(int order, Method method) { }

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<MarkedMethod> markedMethodList = new ArrayList<>();
        checkConfigClass(configClass);
        for (Method method:configClass.getMethods()){
            if (method.isAnnotationPresent(AppComponent.class)) {
                var annotation = method.getAnnotation(AppComponent.class);
                markedMethodList.add(new MarkedMethod(((AppComponent) annotation).order(),method));
            }
        }
        // отсортируем список аннотаций
        markedMethodList.sort((o1,o2)->Integer.compare(o1.order, o2.order));
        // создаем объекты, размещаем их в мапу по имени и в лист в порядке очередности

        Object configClassInst = configClass.getConstructor().newInstance();

        for (MarkedMethod mm:markedMethodList) {
            var typeArr = mm.method.getParameterTypes();
            Object[] args = new Object[typeArr.length];
            // заполним аргументы. При этом свалимся, если чего то будет несоответствовать
            for ( int i =0; i<typeArr.length; i++ ){
                // ищем параметр этого типа в мапе с объедками, записываем в аргументы метода
                for (Object ob: appComponents) {
                    if (typeArr[i].isAssignableFrom(ob.getClass())){
                        args[i] = ob;
                        break;
                    }
                }
                if (args[i] == null) {
                    throw new NoSuchMethodException();
                }
            }
            Object o = mm.method.invoke(configClassInst,args);
            // размещаем в лист и в мапу
            if (appComponentsByName.get(mm.method.getAnnotation(AppComponent.class).name()) != null){
                throw new NoSuchMethodException();
            }
            appComponents.add(o);
            appComponentsByName.put(mm.method.getAnnotation(AppComponent.class).name(),o);
        }

    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws NoSuchMethodException {
        Object obb = null;
        for (Object ob: appComponents){
            if (componentClass.isAssignableFrom(ob.getClass())){
                if (obb != null) {
                    // ошибка двойного нахождения
                    throw new NoSuchMethodException();
                }
              obb = ob;
            }
        }
        // если нашло то возвращаем
        if (obb != null) {
            return (C) obb;
        }
        // не нашло
        throw new NoSuchMethodException();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

}

