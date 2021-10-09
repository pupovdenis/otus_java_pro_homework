package homework;

import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = map.firstEntry();
        if (entry == null) return null;
        Customer resultCustomer =
                new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
        return new AbstractMap.SimpleImmutableEntry<>(resultCustomer, entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = map.higherEntry(customer);
        if (entry == null) return null;
        Customer resultCustomer =
                new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
        return new AbstractMap.SimpleImmutableEntry<>(resultCustomer, entry.getValue());
    }

    public void add(final Customer customer, String data) {
        map.put(customer,data);
    }
}
