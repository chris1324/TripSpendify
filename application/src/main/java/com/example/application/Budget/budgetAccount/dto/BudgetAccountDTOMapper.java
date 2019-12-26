package com.example.application.Budget.budgetAccount.dto;

import com.example.application.Shared.dto.accountRecordDTO.AccountRecordDTO;
import com.example.application.Shared.dtoMapper.AccountMapper;
import com.example.domain.Budget.budgetRecord.BudgetRecord;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Budget.budgetAccount.BudgetAccount;
import com.example.domain.Budget.budgetAccountDetail.BudgetCategoryDetail;

import java.util.List;
import java.util.stream.Collectors;

public class BudgetAccountDTOMapper implements AccountMapper<BudgetAccount, BudgetAccountDTO> {

    @Override
    public BudgetAccountDTO mapToDto(BudgetAccount BudgetAccount) {
        return new BudgetAccountDTO(
                BudgetAccount.tripId.toString(),
                BudgetAccount.mTodayDate.toString(),
                this.mapCategoryDetail(BudgetAccount.mCategoryDetails),
                BudgetAccount.mTodayBudgetAllocated.toString(),
                BudgetAccount.mTodayBalance.toString(),
                BudgetAccount.mToDateBalance.toString(),
                BudgetAccount.mTotalBalance.toString());
    }

    // region helper method ------------------------------------------------------------------------
    private List<BudgetAccountDTO.CategoryDetail> mapCategoryDetail(List<BudgetCategoryDetail> categoryDetails) {
        return categoryDetails.stream()
                .map(categoryDetail ->
                        new BudgetAccountDTO.CategoryDetail(
                                categoryDetail.mCategoryId.toString(),
                                categoryDetail.mIconUri.toString(),
                                categoryDetail.mCategoryName.toString(),
                                this.mapAccountRecord(categoryDetail.mAccRecord),
                                categoryDetail.mTodayBudgetAllocated.toString(),
                                categoryDetail.mTodayBalance.toString(),
                                categoryDetail.mToDateBalance.toString(),
                                categoryDetail.mTotalBalance.toString()))
                .collect(Collectors.toList());
    }

    private List<BudgetAccountDTO.Record> mapAccountRecord(List<BudgetAccount.Record> accRecords) {
        return accRecords.stream()
                .map(record -> new BudgetAccountDTO.Record(
                        record.mDate.getMillisecond(),
                        record.mNote.toString(),
                        record.mAmount.toString(),
                        record.mSourceTransId.toString(),
                        this.mapSourceType(record.mSource),
                        this.mapEffect(record.mSource)))
                .collect(Collectors.toList());
    }

    private AccountRecordDTO.Effect mapEffect(BudgetRecord.SourceType source) {
        return AccountRecordDTO.Effect.valueOf(source);
    }

    private BudgetAccountDTO.SourceType mapSourceType(BudgetRecord.SourceType source) {
        switch (source){
            case TRIP_EXPENSE:
                return  BudgetAccountDTO.SourceType.TRIP_EXPENSE;
            case BUDGET_TRANSACTION:
                return  BudgetAccountDTO.SourceType.BUDGET_TRANSACTION;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method ---------------------------------------------------------------------
}
