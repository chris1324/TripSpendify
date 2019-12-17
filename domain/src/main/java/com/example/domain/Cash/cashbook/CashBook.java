package com.example.domain.Cash.cashbook;

import com.example.domain.Cash.cashrecord.CashRecord;
import com.example.domain.Cash.cashtransaction.CashTransaction;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entity.book.Book;
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

    // region Error Class --------------------------------------------------------------------------
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
    void putCashRecord(CashRecord cashRecord) {
        mCashRecords.put(cashRecord);
    }

    void removeCashRecord(ID sourceTransId) {
        mCashRecords.remove(cashRecord -> cashRecord.getSourceTransId().equals(sourceTransId));
    }

    // -------------------------------------Cash Transaction----------------------------------------
    void putCashTransaction(CashTransaction cashTransaction) {
        mCashTransactions.put(cashTransaction);
        this.putCashRecord(createRecordFromCashTrans(cashTransaction));
    }

    void removeCashTransaction(ID transactionId) {
        mCashTransactions.remove(transactionId);
        this.removeCashRecord(transactionId);
    }

    // region helper method ------------------------------------------------------------------------
    private CashRecord createRecordFromCashTrans(CashTransaction transaction) {
        return CashRecord
                .create(
                        ID.newId(),
                        CashRecord.Source.CASH_TRANSACTION,
                        transaction.getId())
                .getValue()
                .orElseThrow(ImpossibleState::new);
    }
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
