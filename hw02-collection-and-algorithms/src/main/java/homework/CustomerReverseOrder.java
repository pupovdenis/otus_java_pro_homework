package homework;


import java.util.*;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private final Deque<Customer> stack = new ArrayDeque<>();

    public void add(Customer customer) {
        stack.push(customer);
    }

    public Customer take() {
        return stack.poll();
    }
}