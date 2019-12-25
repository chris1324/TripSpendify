package com.example.domain.Cash.cashBook;

import com.example.domain.Cash.cashRecord.CashRecord;
import com.example.domain.Cash.cashTransaction.CashTransaction;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.result.Result;

import java.util.List;

public class CashBook extends Book {
    // region Factory method -----------------------------------------------------------------------
    public static Result<CashBook, CashBook.Err.Create> create(ID tripBookId,
                                                               List<CashRecord> cashTransactions,
                                                               List<CashTransaction> cashDeposits) {
        try {
            Guard.NotNull(tripBookId);
            Guard.NotNull(cashTransactions);
            Guard.NotNull(cashDeposits);
        } catch (NullArgumentException e) {
            return Result.err(CashBook.Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashBook(
                tripBookId,
                EntityList.newList(cashTransactions),
                EntityList.newList(cashDeposits)
        ));
    }

    // endregion Factory method---------------------------------------------------------------------

    // region Error Class -------------------------------`-------------------------------------------
    public static class Err {
        public enum Create {
            NULL_ARGUMENT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final EntityList<CashRecord> mCashRecords;
    private final EntityList<CashTransaction> mCashTransactions;

    private CashBook(ID tripBookId,
                     EntityList<CashRecord> cashRecords,
                     EntityList<CashTransaction> cashTransactions) {
        super(tripBookId);
        mCashRecords = cashRecords;
        mCashTransactions = cashTransactions;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------Cash Record-------------------------------------------
    void saveCashRecord(CashRecord cashRecord) {
        mCashRecords.put(cashRecord);
    }

    @Deprecated
    void removeCashRecord(ID sourceTransId) {
        mCashRecords.remove(cashRecord -> cashRecord.getSourceTransId().equals(sourceTransId));
    }

    // -------------------------------------Cash Transaction----------------------------------------
    void saveCashTransaction(CashTransaction cashTransaction, CashRecord record) {
        mCashTransactions.put(cashTransaction);
        this.saveCashRecord(record);
    }

    void removeCashTransaction(ID transactionId) {
        mCashTransactions.remove(transactionId);
        this.removeCashRecord(transactionId);
    }

    // region helper method ------------------------------------------------------------------------

    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public EntityList<CashRecord> getCashRecords() {
        return mCashRecords.unmodifiable();
    }

    public EntityList<CashTransaction> getCashTransactions() {
        return mCashTransactions.unmodifiable();
    }


    // endregion Getter ----------------------------------------------------------------------------
}
