package ru.pupov.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pupov.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);
    private Class targetClass;
    private Field idField;

    public EntityClassMetaDataImpl(Class targetClass) {
        setTargetClass(targetClass);
    }

    public void setTargetClass(Class targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("target object is null");
        }
        this.idField = getIdFieldList(targetClass).get(0);
        this.targetClass = targetClass;
    }

    private List<Field> getIdFieldList(Class targetClass) {
        var idFields = Arrays.stream(targetClass.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
        if (idFields.size() == 0) {
            LOGGER.error("there isn't 'Id' annotation");
            throw new RuntimeException("there isn't 'Id' annotation");
        }
        if (idFields.size() != 1) {
            LOGGER.error("too much 'Id' annotation");
            throw new RuntimeException("too much 'Id' annotation");
        }
        idFields.get(0).setAccessible(true);
        return idFields;
    }

    @Override
    public String getName() {
        return targetClass.getSimpleName().toLowerCase(Locale.ROOT);
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            for (Constructor<T> nextCostructor : targetClass.getConstructors()) {
                if (nextCostructor.getParameterCount() == 0) {
                    return nextCostructor;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("can't find constructor without arguments");
        }
        return null;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        var fields = getFieldsWithoutId();
        fields.add(0, getIdField());
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        var resultList = new ArrayList<Field>();
        for (var field : targetClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                }
                resultList.add(field);
            }
        }
        return resultList.size() > 0 ? resultList : null;
    }
}
