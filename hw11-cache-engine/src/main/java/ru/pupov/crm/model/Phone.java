package ru.pupov.crm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phones")
public class Phone implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone() {
    }

    public Phone(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public Phone setId(long id) {
        this.id = id;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Phone setNumber(String number) {
        this.number = number;
        return this;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    protected Phone clone() throws CloneNotSupportedException {
        return new Phone(this.id, this.number);
    }

    public List<Phone> getPhoneFakes(int numberOfPhones) {
        if (numberOfPhones < 1) return null;
        List<Phone> phones = new ArrayList<>();
        for (int i = 0; i < numberOfPhones; i++) {
            phones.add(new Phone()
                    .setId(i)
                    .setNumber("+1 234 " + i)
            );
        }
        return phones;
    }
}
