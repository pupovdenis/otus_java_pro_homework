package banknotes;

import enums.CurrencyEnum;

import java.lang.reflect.Field;

public abstract class BankNote implements Cloneable {
    private final CurrencyEnum currency;
    private final Integer nominal;

    public BankNote(CurrencyEnum currency, Integer nominal) {
        this.currency = currency;
        this.nominal = nominal;
    }

    public BankNote(BankNote target) {
        this.currency = target.currency;
        this.nominal = target.nominal;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public Integer getNominal() {
        return nominal;
    }

    @Override
    public BankNote clone() {
        try {
            BankNote clone = (BankNote) super.clone();
            Field nominalField = BankNote.class.getDeclaredField("nominal");
            nominalField.setAccessible(true);
            nominalField.set(clone, nominal);
            return clone;
        } catch (CloneNotSupportedException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ошибка. Не удалось клонировать объект", e);
        }
    }
}
