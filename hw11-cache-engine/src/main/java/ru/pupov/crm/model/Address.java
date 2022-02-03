package ru.pupov.crm.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "addresses")
public class Address implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

    public Address() {
    }

    @Override
    protected Address clone() throws CloneNotSupportedException {
        return new Address(this.id, this.street);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Address(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }

    public Address getFake() {
        this.setStreet("street");
        return this;
    }
}
