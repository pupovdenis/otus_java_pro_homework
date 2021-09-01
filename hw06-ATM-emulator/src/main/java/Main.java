import banknotes.BankNote;
import factory.BankNoteFactory;
import factory.BankNoteFactoryRUS;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATMImpl();
        BankNoteFactory factory = new BankNoteFactoryRUS();
        BankNote bankNote10ru = factory.createBankNote(10);
        BankNote bankNote50ru = factory.createBankNote(50);
        BankNote bankNote100ru = factory.createBankNote(100);
        BankNote bankNote200ru = factory.createBankNote(200);
        BankNote bankNote500ru = factory.createBankNote(500);
        BankNote bankNote1000ru = factory.createBankNote(1000);
        BankNote bankNote2000ru = factory.createBankNote(2000);
        BankNote bankNote5000ru = factory.createBankNote(5000);

        System.out.println(atm.getBalanceReport());

        Map<BankNote, Integer> bagWithCash = new HashMap<>();
        bagWithCash.put(bankNote10ru, 10);
        bagWithCash.put(bankNote50ru, 10);
        bagWithCash.put(bankNote100ru, 10);
        bagWithCash.put(bankNote200ru, 10);
        bagWithCash.put(bankNote500ru, 10);
        bagWithCash.put(bankNote1000ru, 10);
        bagWithCash.put(bankNote2000ru, 10);
        bagWithCash.put(bankNote5000ru, 10);

        atm.putCash(bagWithCash);

        System.out.println(atm.getBalanceReport());

        try {
            atm.takeCash(100);
            System.out.println(atm.getBalanceReport());

            atm.takeCash(10120);
            System.out.println(atm.getBalanceReport());

//            atm.takeCash(1000000);
//            System.out.println(atm.getBalanceReport());

            atm.takeCash(2);
            System.out.println(atm.getBalanceReport());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
