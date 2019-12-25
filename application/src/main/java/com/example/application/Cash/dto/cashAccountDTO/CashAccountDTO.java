package com.example.application.Cash.dto.cashAccountDTO;

import com.example.application.Shared.dto.accountRecordDTO.AccountRecordDTO;
import com.example.application.Shared.dto.accountDTO.AccountDTO;

import java.util.List;

public class CashAccountDTO extends AccountDTO {

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


    public final String mTotalBalance;
    public final List<Record> mAccRecords;

    public CashAccountDTO(String tripId, String totalBalance, List<Record> accRecords) {
        super(tripId);
        mTotalBalance = totalBalance;
        mAccRecords = accRecords;
    }

    // region SourceTypeDTO ------------------------------------------------------------------------
    public enum SourceType {
        TRIP_EXPENSE,
        CASH_DEPOSIT,
        CASH_WITHDRAWAL,
        CASH_ADJUST_UP,
        CASH_ADJUST_DOWN,
        COLL_SETTLEMENT_MADE,
        COLL_SETTLEMENT_ACCEPTED,
        COLL_CONTRIBUTION_MADE,
        COLL_CONTRIBUTION_ACCEPTED
    }
    // endregion SourceTypeDTO  --------------------------------------------------------------------


}
