package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl <T> implements EntityClassMetaData{
    private final Class<?> clazz;
    public EntityClassMetaDataImpl(Class<?> clazz){
        this.clazz = clazz;
    }
    @Override
    public String getName() {

        return clazz.getName().replace(clazz.getPackageName(),"").replace(".","");
    }

    @Override
    public Constructor<T> getConstructor() throws NoSuchMethodException {
        List<Field> listField = getAllFields();
        Class[] prmtArr = new Class[listField.size()];

        for (int i=0; i<prmtArr.length;i++){
           prmtArr[i] = listField.get(i).getType();
        }
        return (Constructor<T>) clazz.getConstructor(prmtArr);
    }

    @Override
    public Field getIdField() {
        for (Field field: clazz.getDeclaredFields()){
            for (Annotation annotation:field.getAnnotations()){
                if (annotation.annotationType().equals(Id.class)){
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(o->!o.equals(getIdField()))
                .toList();
    }
}
