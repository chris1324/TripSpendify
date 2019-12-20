package com.example.domain.Cash.cashbook;

import com.example.domain.Cash.cashrecord.CashRecord;
import com.example.domain.Cash.cashtransaction.CashTransaction;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.baseclass.book.Book;
import com.example.domain.Common.entitylist.EntityList;
import com.example.domain.Common.errorhanding.exception.ImpossibleState;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;

import java.util.List;

public class CashBook extends Book {
    // region Factory method -----------------------------------------------------------------------
    public static Result<CashBook, CashBook.Err.Create> create(ID id,
                                                               ID tripBookId,
                                                               List<CashRecord> cashTransactions,
                                                               List<CashTransaction> cashDeposits) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(tripBookId);
            Guard.NotNull(cashTransactions);
            Guard.NotNull(cashDeposits);
        } catch (NullArgumentException e) {
            return Result.err(CashBook.Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new CashBook(
                id,
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

    private CashBook(ID id,
                     ID tripBookId,
                     EntityList<CashRecord> cashRecords,
                     EntityList<CashTransaction> cashTransactions) {
        super(id, tripBookId);
        mCashRecords = cashRecords;
        mCashTransactions = cashTransactions;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------Cash Record-------------------------------------------
    void addCashRecord(CashRecord cashRecord) {
        mCashRecords.put(cashRecord);
    }

    void removeCashRecord(ID sourceTransId) {
        mCashRecords.remove(cashRecord -> cashRecord.getSourceTransId().equals(sourceTransId));
    }

    // -------------------------------------Cash Transaction----------------------------------------
    void addCashTransaction(CashTransaction cashTransaction, CashRecord record) {
        mCashTransactions.put(cashTransaction);
        this.addCashRecord(record);
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
