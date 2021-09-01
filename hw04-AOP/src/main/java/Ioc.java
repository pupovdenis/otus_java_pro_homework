import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {
    public static SomeClass createClass() {
        InvocationHandler handler = new MyInvocationHandler(new SomeClassImpl());
        return (SomeClass) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class[]{SomeClass.class}, handler);
    }
}
