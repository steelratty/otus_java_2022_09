package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{
    private final EntityClassMetaData entityClassMetaData;
    private String selectAllSql;
    private String selectByIdSql;
    private String insertSql;
    private String updateSql;


    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }
    private String getStrFromField(String ss, boolean withId){
        String s = "";
        List<Field> listField;
        if (withId) {
            listField = entityClassMetaData.getAllFields();
        }
        else
        {
            listField = entityClassMetaData.getFieldsWithoutId();
        }

        for (Field field:listField){
                s = s + field.getName() + ss;
        }
        return s.substring( 0, s.length() - 1 );
    }

    @Override
    public EntityClassMetaData getEntityClassMetaData() {
        return entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        if (selectAllSql == null) {
            selectAllSql = "select "+ getStrFromField(",",true) +" from "+ entityClassMetaData.getName();
        }
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        if (selectByIdSql == null) {
            selectByIdSql = "select " + getStrFromField(",", true) + " from " + entityClassMetaData.getName()
                    + " where " + entityClassMetaData.getIdField().getName() + " = " + " ?";
        }
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (insertSql == null ) {
            String ss = "";
            int fldcnt = entityClassMetaData.getAllFields().size();
            for (int i = 1; i < fldcnt; i++) {
                ss = ss + "?,";
            }
            ss = ss.substring(0, ss.length() - 1);
            insertSql = "insert into " + entityClassMetaData.getName() + "(" + getStrFromField(",", false) + ") values (" + ss + ")";
        }
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (updateSql==null) {
            updateSql = "update " + entityClassMetaData.getName() + " set " + getStrFromField("=?,", false) + " where " + entityClassMetaData.getIdField().getName() + "=?";
        }
        return updateSql;
    }
}
