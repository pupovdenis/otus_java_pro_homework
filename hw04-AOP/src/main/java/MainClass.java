public class MainClass {
    public static void main(String[] args) {
        SomeClass someClass = Ioc.createClass();
        someClass.someMethod();
        someClass.someMethodForAOP();
        someClass.someMethodForTestAOP("test", 1);
        someClass.someMethodForTestAOP(1, 1);
    }
}
