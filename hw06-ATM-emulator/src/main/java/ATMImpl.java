import banknotes.BankNote;
import enums.StatusEnum;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ATMImpl implements ATM, Cloneable {

    private Map<BankNote, Integer> cash;
    private StatusEnum status;
    private Long commonBalance;

    public ATMImpl() {
        this.cash = new HashMap<>();
        this.status = StatusEnum.EMPTY;
        this.commonBalance = 0L;
    }

    public void setCash(Map<BankNote, Integer> cash) {
        this.cash = cash;
    }

    @Override
    protected ATMImpl clone() throws CloneNotSupportedException {
        ATMImpl atm = (ATMImpl) super.clone();
        Map<BankNote, Integer> cloneMap = new HashMap<>();
        cash.forEach((key, value) -> cloneMap.put(key.clone(), value));
        atm.setCash(cloneMap);
        return atm;
    }

    @Override
    public Map<BankNote, Integer> takeCash(Integer summa) {
        if (status.equals(StatusEnum.EMPTY) || summa > commonBalance)
            throw new RuntimeException("Запрошенная сумма не может быть выдана. Недостаточно средств");
        if (summa < 0) throw new RuntimeException("Ошибка. Запрошена отрицательная сумма");

        ATMImpl snapshot;
        try {
            snapshot = this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Ошибка");
        }

        Map<BankNote, Integer> result = new HashMap<>();

        try {
            System.out.println(doTakeCash(summa, result));
        } catch (Exception e) {
            this.cash = snapshot.cash;
            this.commonBalance = snapshot.commonBalance;
            throw new RuntimeException("Ошибка при выдаче запрашиваемой суммы");
        }
        if (commonBalance == 0) this.status = StatusEnum.EMPTY;
        return result;
    }

    private String doTakeCash(Integer summa, Map<BankNote, Integer> result) {
        AtomicLong remain = new AtomicLong(summa);
        List<BankNote> sortedBankNotes = cash.keySet().stream()
                .sorted(Comparator.comparingInt(BankNote::getNominal).reversed())
                .collect(Collectors.toList());
        for (BankNote bankNote : sortedBankNotes) {
            int buf = remain.intValue() / bankNote.getNominal();
            int val = cash.get(bankNote);
            if (val > 0 && buf > 0) {
                int min = Math.min(buf, val);
                long all = (long) min * bankNote.getNominal();
                commonBalance -= all;
                cash.put(bankNote, val - min);
                remain.addAndGet(-all);
                result.put(bankNote, min);
            }
            if (remain.intValue() == 0) break;
        }
        if (remain.intValue() > 0)
            throw new RuntimeException("Ошибка. Недостаточно купюр. Попробуйте ввести другую сумму");
        return "Запрошенная сумма выдана";
    }

    @Override
    public boolean putCash(Map<BankNote, Integer> bankNotes) {
        try {
            bankNotes.forEach((key, value) -> {
                if (value < 0) throw new RuntimeException("Ошибка. Попытка положить отрицательное число банкнот");
                cash.put(key, value);
                commonBalance += (long) key.getNominal() * value;
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (status == StatusEnum.EMPTY) status = StatusEnum.NORMAL;
        return true;
    }

    @Override
    public String getBalanceReport() {
        return status == StatusEnum.EMPTY ? "Банкомат пуст" : ("Баланс банкомата:\n" + this);
    }

    @Override
    public String toString() {
        return cash.entrySet().stream()
                .map(entry -> entry.getKey().getCurrency().getSymbol() + entry.getKey().getNominal() + "\t" + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}
