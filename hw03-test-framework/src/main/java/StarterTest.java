import annotations.After;
import annotations.Before;
import annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * "Класс-запускалка" теста
 */
public class StarterTest {
    private int test_counter = 0;
    private int test_successful_counter = 0;
    private int test_unsuccessful_counter = 0;

    public static void main(String[] args) {
        StarterTest starterTest = new StarterTest();
        System.out.println(starterTest.runTest(ClassTest.class.getSimpleName()));
    }

    /**
     * Метод запуска теста
     * @param className наименование тестируемого класса
     * @return Отчет в формате String
     */
    public String runTest(String className) {
        test_counter = 0;
        test_successful_counter = 0;
        test_unsuccessful_counter = 0;
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            if (!clazz.isAnnotationPresent(Test.class))
                return "Ошибка. Класс не помечен аннотацией @Test";
            Constructor<?> constructor = Class.forName(className).getConstructor();
            for (Method method : getAnnotatedMethods(constructor.newInstance(), Test.class)) {
                Object testObject = constructor.newInstance();
                doMethods(testObject, Before.class);
                doTestMethod(method, testObject, true);
                doMethods(testObject, After.class);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return doReport(clazz);
    }

    /**
     * Запустить метод тестируемого класса
     * @param testObject экземпляр тестируемого класса
     * @param method запускаемый метод
     * @param isNeedIncludeToReport нужно ли результаты проверки включить в отчет
     */
    private void doTestMethod(Method method, Object testObject, boolean isNeedIncludeToReport) {
        if (method == null || testObject == null) return;
        if (isNeedIncludeToReport) {
            test_counter++;
            try {
                method.invoke(testObject);
                test_successful_counter++;
            } catch (Exception e) {
                test_unsuccessful_counter++;
            }
        } else {
            try {
                method.invoke(testObject);
            } catch (Exception e) {
                e.printStackTrace();
                //do nothing
            }
        }
    }

    /**
     * Запустить методы тестируемого класса
     * @param testObject экземпляр тестируемого класса
     * @param annotationClass требуемая аннотация для метода
     */
    private void doMethods(Object testObject, Class<? extends Annotation> annotationClass) {
        if (testObject == null) return;
        Method[] methods = getAnnotatedMethods(testObject, annotationClass);
        if (methods.length < 1) return;
        for (Method method : methods) {
            doTestMethod(method, testObject, false);
        }
    }

    /**
     * Получить аннотированные методы экземпляра тестируемого класса
     * @param testObject экземпляр тестируемого класса
     * @param annotationClass требуемая аннотация для методов
     * @return массив методов
     */
    private Method[] getAnnotatedMethods(Object testObject, Class<? extends Annotation> annotationClass) {
        List<Method> methodList = new ArrayList<>();
        for (Method method : testObject.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) methodList.add(method);
        }
        return methodList.toArray(new Method[0]);
    }

    /**
     * Создать отчет о выполненных тестах
     * @param clazz тестируемый класс
     * @return строка
     */
    private String doReport(Class<?> clazz) {
        if (clazz == null) return "Ошибка. Тестируемый класс = null";
        if (test_counter == 0) return "Ошибка. Не выполнено ни одного теста";
        return "\nРезультаты тестирования класса " + clazz.getSimpleName() + ":\n" +
                "Протестировано методов - " + test_counter + "\n" +
                "- успешно: " + test_successful_counter + "\n" +
                "- неудачно: " + test_unsuccessful_counter;
    }
}
