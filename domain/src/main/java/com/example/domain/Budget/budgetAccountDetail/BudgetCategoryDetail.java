package com.example.domain.Budget.budgetAccountDetail;

import com.example.domain.Budget.budgetAccount.BudgetAccount;
import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Shared.queryBaseClass.account.Account;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.uri.Uri;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BudgetCategoryDetail {

    // region Variables and Constructor ------------------------------------------------------------
    private final Date mTodayDate;
    private final Account.Calculator mCalculator;

    public final ID mCategoryId;
    public final Uri mIconUri;
    public final Name mCategoryName;
    public final List<BudgetAccount.Record> mAccRecord;

    public final Amount mTodayBudgetAllocated;
    public final Amount mTodayBalance;
    public final Amount mToDateBalance;
    public final Amount mTotalBalance;

    public BudgetCategoryDetail(ID categoryId, Uri iconUri, Name categoryName, List<BudgetAccount.Record> recordDetails) {
        mTodayDate = Date.today();
        mCalculator = Account.calculator();

        mCategoryId = categoryId;
        mIconUri = iconUri;
        mCategoryName = categoryName;
        mAccRecord = recordDetails;

        mTodayBudgetAllocated = this.calculateTodayBudgetAllocated(recordDetails);
        mTodayBalance = this.calculateTodayBalance(recordDetails);
        mToDateBalance = this.calculateToDateBalance(recordDetails);
        mTotalBalance = this.calculateTotalBalance(recordDetails);
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    private Amount calculateTodayBudgetAllocated(List<BudgetAccount.Record> recordDetails) {
        return this.calculateBalance(
                recordDetails,
                recordDetail -> this.isBudgetTrans(recordDetail.mSource) && this.isToday(recordDetail.mDate));
    }

    private Amount calculateTodayBalance(List<BudgetAccount.Record> recordDetails) {
        return this.calculateBalance(
                recordDetails,
                recordDetail -> this.isToday(recordDetail.mDate));
    }

    private Amount calculateToDateBalance(List<BudgetAccount.Record> recordDetails) {
        return this.calculateBalance(
                recordDetails,
                recordDetail -> this.isTodayOrBefore(recordDetail.mDate));
    }

    private Amount calculateTotalBalance(List<BudgetAccount.Record> accRecord) {
        return this.calculateBalance(
                accRecord,
                record -> true);
    }

    // region helper method ------------------------------------------------------------------------
    private Amount calculateBalance(List<BudgetAccount.Record> accRecords, Predicate<BudgetAccount.Record> predicate) {
        // Filter
        List<BudgetAccount.Record> filteredAccRecords = accRecords.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        // Calculate & Return
        return mCalculator.calculateBalance(filteredAccRecords);
    }

    private boolean isToday(Date date) {
        return date.isSameDate(mTodayDate);
    }

    private boolean isTodayOrBefore(Date date) {
        return date.isSameDate(mTodayDate) || date.isBefore(mTodayDate);
    }


    private boolean isBudgetTrans(BudgetRecord.SourceType source) {
        return source == BudgetRecord.SourceType.BUDGET_TRANSACTION;
    }
    // endregion helper method ---------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
}
