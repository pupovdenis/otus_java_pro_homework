import annotation.Log;

public interface SomeClass {
    void someMethod();

    void someMethodForAOP();

    void someMethodForTestAOP(String str, Integer n);

    void someMethodForTestAOP(Integer a, Integer b);
}
