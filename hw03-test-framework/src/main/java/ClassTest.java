import annotations.After;
import annotations.Before;
import annotations.Test;

import java.util.Random;

@Test
public class ClassTest {

    @Before
    public boolean beforeMethod() {
        System.out.println("You're in beforeMethod");
        return true;
    }

    @Test
    public void testMethod1() {
        if (new Random().nextBoolean()) {
            System.out.println("You're in testMethod");
        } else {
            throw new RuntimeException("Exception in testMethod");
        }
    }

    @After
    public void afterMethod() {
        System.out.println("You're in afterMethod");
    }
}
