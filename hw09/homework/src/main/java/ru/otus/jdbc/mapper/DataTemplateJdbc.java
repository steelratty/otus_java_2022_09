package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }


    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
                    List<Field> listField = entityClassMetaData.getAllFields();
                    Object[] prmtArr = new Object[listField.size()];

                    for (int i=0; i<prmtArr.length;i++){
                        prmtArr[i] = (rs.getObject(listField.get(i).getName()));
                    }

                     Constructor<T> clntConsr = entityClassMetaData.getConstructor();
                    return  clntConsr.newInstance(prmtArr);
                }
                return null;
            } catch (SQLException | NoSuchMethodException e) {
                throw new DataTemplateException(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
     //   throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findAll(Connection connection) {
        List<T> ret  = new ArrayList<>();
        dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), new ArrayList<Object>() , rs -> {
           // return clntConsr.newInstance(prmtArr);
            try {
                while (rs.next()) {
                    var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
                    List<Field> listField = entityClassMetaData.getAllFields();
                    Object[] prmtArr = new Object[listField.size()];

                    for (int i = 0; i < prmtArr.length; i++) {
                        prmtArr[i] = (rs.getObject(listField.get(i).getName()));
                    }

                    Constructor<T> clntConsr = entityClassMetaData.getConstructor();
                    ret.add(clntConsr.newInstance(prmtArr));

                }
                return null;
            } catch (SQLException | NoSuchMethodException e) {
                throw new DataTemplateException(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
        return ret;
        //   throw new UnsupportedOperationException();
    }


    @Override
    public long insert(Connection connection, T client) throws IllegalAccessException {
        var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
        List<Field> listField = entityClassMetaData.getFieldsWithoutId();
        List<Object> listVal = new ArrayList<>();
        for (Field field: listField){
           field.setAccessible(true);
           listVal.add(field.get(client));
        }
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), listVal);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) throws IllegalAccessException {
        var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
        List<Field> listField = entityClassMetaData.getFieldsWithoutId();
        List<Object> listVal = new ArrayList<>();
        for (Field field: listField){
            field.setAccessible(true);
            listVal.add(field.get(client));
        }
        try {
             dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), listVal);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
