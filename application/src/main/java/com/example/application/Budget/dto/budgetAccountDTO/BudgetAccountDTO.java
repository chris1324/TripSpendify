package com.example.application.Budget.dto.budgetAccountDTO;

import com.example.application.Shared.dto.accountRecordDTO.AccountRecordDTO;
import com.example.application.Shared.dto.accountDTO.AccountDTO;

import java.util.List;

public class BudgetAccountDTO extends AccountDTO {

    // region RecordDTO ----------------------------------------------------------------------------
    public static class Record extends AccountRecordDTO<SourceType> {
        public Record(long date,
                      String note,
                      String recordAmount,
                      String sourceTransId,
                      SourceType sourceType,
                      Effect effect) {
            super(date,
                    note,
                    recordAmount,
                    sourceTransId,
                    sourceType,
                    effect);
        }
    }
    // endregion RecordDTO  ------------------------------------------------------------------------

    // region SourceTypeDTO ------------------------------------------------------------------------
    public enum SourceType {
        TRIP_EXPENSE,
        BUDGET_TRANSACTION;
    }

    // endregion SourceTypeDTO  --------------------------------------------------------------------
    public final String mTodayDate;
    public final List<CategoryDetail> mCategoryDetails;

    public final String mTodayBudgetAllocated;
    public final String mTodayBalance;
    public final String mToDateBalance;
    public final String mTotalBalance;

    public BudgetAccountDTO(String tripId,
                            String todayDate,
                            List<CategoryDetail> categoryDetails,
                            String todayBudgetAllocated,
                            String todayBalance, String toDateBalance,
                            String totalBalance) {
        super(tripId);
        mTodayDate = todayDate;
        mCategoryDetails = categoryDetails;
        mTodayBudgetAllocated = todayBudgetAllocated;
        mTodayBalance = todayBalance;
        mToDateBalance = toDateBalance;
        mTotalBalance = totalBalance;
    }

    // region CategoryDetailDTO --------------------------------------------------------------------
    public static class CategoryDetail {
        public final String mCategoryId;
        public final String mIconUri;
        public final String mCategoryName;
        public final List<Record> mAccRecord;

        public final String mTodayBudgetAllocated;
        public final String mTodayBalance;
        public final String mToDateBalance;
        public final String mTotalBalance;

        public CategoryDetail(String categoryId,
                              String iconUri,
                              String categoryName,
                              List<Record> accRecord,
                              String todayBudgetAllocated,
                              String todayBalance,
                              String toDateBalance,
                              String totalBalance) {
            mCategoryId = categoryId;
            mIconUri = iconUri;
            mCategoryName = categoryName;
            mAccRecord = accRecord;
            mTodayBudgetAllocated = todayBudgetAllocated;
            mTodayBalance = todayBalance;
            mToDateBalance = toDateBalance;
            mTotalBalance = totalBalance;
        }
    }

    // endregion CategoryDetailDTO  ----------------------------------------------------------------
}
