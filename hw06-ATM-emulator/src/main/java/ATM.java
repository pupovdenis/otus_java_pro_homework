import banknotes.BankNote;

import java.util.List;
import java.util.Map;

public interface ATM {
    Map<BankNote, Integer> takeCash(Integer summa);
    boolean putCash(Map<BankNote, Integer> bankNotes);
    String getBalanceReport();
}
