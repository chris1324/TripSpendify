package com.example.domain.Budget.budgetBook;

import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.valueObject.date.Date;

import java.util.List;

public class BudgetBook extends Book {

    // region Factory method -----------------------------------------------------------------------
    public static Result<BudgetBook, Err.Create> create(ID tripBookID,
                                                        List<BudgetRecord> budgetRecords,
                                                        List<BudgetTransaction> budgetTransactions) {

        try {
            Guard.NotNull(tripBookID);
            Guard.NotNull(budgetRecords);
            Guard.NotNull(budgetTransactions);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new BudgetBook(
                tripBookID,
                EntityList.newList(budgetRecords),
                EntityList.newList(budgetTransactions)
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
    private final EntityList<BudgetRecord> mBudgetRecords;
    private final EntityList<BudgetTransaction> mBudgetTransactions;

    protected BudgetBook(ID tripBookID,
                         EntityList<BudgetRecord> budgetRecords,
                         EntityList<BudgetTransaction> budgetTransactions) {
        super(tripBookID);
        mBudgetRecords = budgetRecords;
        mBudgetTransactions = budgetTransactions;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // -------------------------------------Budget Record-------------------------------------------
    void saveBudgetRecord(BudgetRecord record) {
        mBudgetRecords.put(record);
    }

    @Deprecated
    void removeBudgetRecord(ID sourceTransId) {
        mBudgetRecords.remove(record -> record.getSourceTransId().equals(sourceTransId));
    }

    // -----------------------------------Budget Transaction----------------------------------------
    void saveBudgetTransaction(BudgetTransaction transaction, BudgetRecord budgetRecord) {
        this.removeBudgetTransWithThisDay(transaction.getDate());
        mBudgetTransactions.put(transaction);
        this.saveBudgetRecord(budgetRecord);
    }

    void removeBudgetTransaction(ID transactionId) {
        mBudgetTransactions.remove(transactionId);
        this.removeBudgetRecord(transactionId);
    }

    // region helper method ------------------------------------------------------------------------
    private void removeBudgetTransWithThisDay(Date date) {
        mBudgetTransactions
                .searchFirst(budgetTransaction -> budgetTransaction.getDate().isSameDate(date))
                .map(BudgetTransaction::getId)
                .ifPresent(this::removeBudgetTransaction);
    }

    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public EntityList<BudgetRecord> getBudgetRecords() {
        return mBudgetRecords.unmodifiable();
    }

    public EntityList<BudgetTransaction> getBudgetTransactions() {
        return mBudgetTransactions.unmodifiable();
    }

    // endregion Getter ----------------------------------------------------------------------------
}
