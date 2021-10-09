package factory;

import banknotes.BankNote;
import banknotes.rub.*;

public class BankNoteFactoryRUS implements BankNoteFactory {
    @Override
    public BankNote createBankNote(Integer nominal) {
        return switch (nominal) {
            case 10 -> new BankNote10RUB();
            case 50 -> new BankNote50RUB();
            case 100 -> new BankNote100RUB();
            case 200 -> new BankNote200RUB();
            case 500 -> new BankNote500RUB();
            case 1000 -> new BankNote1000RUB();
            case 2000 -> new BankNote2000RUB();
            case 5000 -> new BankNote5000RUB();
            default -> throw new IllegalStateException("Unexpected value: " + nominal);
        };
    }
}
