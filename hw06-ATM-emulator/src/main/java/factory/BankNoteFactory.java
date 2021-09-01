package factory;

import banknotes.BankNote;

public interface BankNoteFactory {
    BankNote createBankNote(Integer nominal);
}
