package ru.pupov.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);
    public EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        setEntityClassMetaDataAndTableName(entityClassMetaData);
    }

    public void setEntityClassMetaDataAndTableName(EntityClassMetaData entityClassMetaData) {
        if (entityClassMetaData == null)
            LOGGER.error("entitySQLMetaData is null", new RuntimeException());
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public EntityClassMetaData getEntityClassMetaData() {
        return this.entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select %s from %s", getStrColumnNames(entityClassMetaData.getAllFields()), entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return getSelectAllSql() + " where " + entityClassMetaData.getIdField().getName() + "=?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsList = entityClassMetaData.getFieldsWithoutId();
        return String.format("insert into %s(%s) values (%s)",
                entityClassMetaData.getName(), getStrColumnNames(fieldsList), getStrFormattedValues(fieldsList.size()));
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldsList = entityClassMetaData.getAllFields();
        String settingsFields = fieldsList.stream()
                .map(field -> field.getName() + "=?")
                .collect(Collectors.joining(", "));
        return String.format("update %s set %s where %s=?",
                entityClassMetaData.getName(), settingsFields, entityClassMetaData.getIdField().getName());
    }

    private String getStrColumnNames(List<Field> fieldsList) {
        return fieldsList.stream()
                .map(field -> field.getName().toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(","));
    }

    private String getStrFormattedValues(int numOfValues) {
        return IntStream.range(0, numOfValues)
                .mapToObj(i -> "?")
                .collect(Collectors.joining(", "));
    }
}
