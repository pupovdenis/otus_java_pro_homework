import annotation.Log;

public class SomeClassImpl implements SomeClass{
    @Override
    public void someMethod() {
        printCurrentMethod();
    }

    @Log
    @Override
    public void someMethodForAOP() {
        printCurrentMethod();
    }

    @Log
    @Override
    public void someMethodForTestAOP(String str, Integer n) {
        printCurrentMethod();
    }

    @Log
    @Override
    public void someMethodForTestAOP(Integer a, Integer b) {
        printCurrentMethod();
    }

    private void printCurrentMethod() {
        System.out.println("In " + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
