import annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MyInvocationHandler implements InvocationHandler {

    SomeClass someClass;
    Set<String> annotatedMethodsUniqStrSet;

    public MyInvocationHandler(SomeClass someClass) {
        System.out.println(Arrays.toString(someClass.getClass().getDeclaredMethods()[0].getParameterTypes()));
        this.someClass = someClass;
        annotatedMethodsUniqStrSet = Arrays.stream(someClass.getClass().getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(Log.class))
                .map(this::getMethodUniqueStr)
                .collect(Collectors.toSet());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Log.class)) System.out.println(getLogStr(method, args));
        if (annotatedMethodsUniqStrSet.contains(getMethodUniqueStr(method))) System.out.println(getLogStr(method, args));
        return method.invoke(someClass, args);
    }

    private String getLogStr(Method method, Object[] args) {
        String resultStr = "executed method: " + method.getName();
        return args == null || args.length < 1 ? resultStr + ", without params"
                : resultStr + ", param" + (args.length > 1 ? "s: " : ": ")
                + Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "));
    }

    private String getMethodUniqueStr(Method method) {
        return method.getName() + Arrays.toString(method.getParameterTypes());
    }
}
