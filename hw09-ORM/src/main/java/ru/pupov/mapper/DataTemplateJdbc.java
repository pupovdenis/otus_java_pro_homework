package ru.pupov.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pupov.core.repository.DataTemplate;
import ru.pupov.core.repository.DataTemplateException;
import ru.pupov.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private static final Logger logger = LoggerFactory.getLogger(DataTemplateJdbc.class);
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
                    Constructor<T> constructor = entitySQLMetaData.getEntityClassMetaData().getConstructor();
                    List<Field> fields = entitySQLMetaData.getEntityClassMetaData().getAllFields();

                    T result = constructor.newInstance();

                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        for (Field field : fields) {
                            if (field.getName().equalsIgnoreCase(metaData.getColumnName(i))) {
                                field.set(result, rs.getObject(field.getName()));
                            }
                        }
                    }
                    return result;
                }
                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), Collections.emptyList(), rs -> {
            try {
                List<T> resultList = new ArrayList<>();
                Constructor<T> constructor = entitySQLMetaData.getEntityClassMetaData().getConstructor();
                while (rs.next()) {
                    Parameter[] parameters = constructor.getParameters();
                    Object[] objects = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        objects[i] = rs.getObject(parameters[i].getName(), parameters[i].getType());
                    }
                    resultList.add(constructor.newInstance(objects));
                }
                return resultList;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        List<Field> fields = entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId();
        List<Object> resultList = new ArrayList<>();
        for (Field field : fields) {
            try {
                resultList.add(field.get(object));
            } catch (IllegalAccessException e) {
                logger.warn("can't get field{} from object{}", field.getName(), object);
                throw new DataTemplateException(e);
            }
        }
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), resultList);
    }

    @Override
    public void update(Connection connection, T object) {
        List<Field> fields = entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId();
        List<Object> resultList = new ArrayList<>();
        for (Field field : fields) {
            try {
                resultList.add(field.get(object));
            } catch (IllegalAccessException e) {
                logger.warn("can't get field{} from object{}", field.getName(), object);
                throw new DataTemplateException(e);
            }
        }
        resultList.add(entitySQLMetaData.getEntityClassMetaData().getIdField());
        dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), resultList);
    }
}
