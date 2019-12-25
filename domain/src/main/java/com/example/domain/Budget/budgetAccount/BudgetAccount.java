package com.example.domain.Budget.budgetAccount;

import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Budget.budgetAccountDetail.BudgetCategoryDetail;
import com.example.domain.Shared.queryBaseClass.account.Account;
import com.example.domain.Shared.queryBaseClass.acountRecord.AccountRecord;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BudgetAccount extends Account {

    // region Record -------------------------------------------------------------------------------
    public class Record extends AccountRecord<BudgetRecord.SourceType> {
        public Record(ID sourceTransId, BudgetRecord.SourceType source, Amount amount, Date date, Note note) {
            super(sourceTransId, source, amount, date, note);
        }
    }
    // endregion Record  ---------------------------------------------------------------------------

    public final Date mTodayDate;
    public final List<BudgetCategoryDetail> mCategoryDetails;

    public final Amount mTodayBudgetAllocated;
    public final Amount mTodayBalance;
    public final Amount mToDateBalance;
    public final Amount mTotalBalance;

    public BudgetAccount(ID tripId, List<BudgetCategoryDetail> categoryDetails) {
        super(tripId);
        mTodayDate = Date.today();
        mCategoryDetails = categoryDetails;

        mTodayBudgetAllocated = this.calculateTodayBudgetAllocated(categoryDetails);
        mTodayBalance = this.calculateTodayBalance(categoryDetails);
        mToDateBalance = this.calculateToDateBalance(categoryDetails);
        mTotalBalance = this.calculateTotalBalance(categoryDetails);
    }

    private Amount calculateTodayBudgetAllocated(List<BudgetCategoryDetail> categoryDetails) {
        return this.calculateBalance(
                categoryDetails,
                categoryDetail -> categoryDetail.mTodayBudgetAllocated);
    }

    private Amount calculateTodayBalance(List<BudgetCategoryDetail> categoryDetails) {
        return this.calculateBalance(
                categoryDetails,
                categoryDetail -> categoryDetail.mTodayBalance);
    }

    private Amount calculateToDateBalance(List<BudgetCategoryDetail> categoryDetails) {
        return this.calculateBalance(
                categoryDetails,
                categoryDetail -> categoryDetail.mToDateBalance);
    }

    private Amount calculateTotalBalance(List<BudgetCategoryDetail> categoryDetails) {
        return this.calculateBalance(
                categoryDetails,
                categoryDetail -> categoryDetail.mTotalBalance);
    }

    // region helper method ------------------------------------------------------------------------
    private Amount calculateBalance(List<BudgetCategoryDetail> categoryDetails, Function<BudgetCategoryDetail, Amount> mapFunction) {
        // Map
        List<Amount> balance = categoryDetails.stream()
                .map(mapFunction)
                .collect(Collectors.toList());
        // Calculate & Return
        return Amount.calculator().sum(balance);
    }
    // endregion helper method ---------------------------------------------------------------------
}
