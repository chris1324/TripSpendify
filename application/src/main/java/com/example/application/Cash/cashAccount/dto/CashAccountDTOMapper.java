package com.example.application.Cash.cashAccount.dto;

import com.example.application.Shared.dto.accountRecordDTO.AccountRecordDTO;
import com.example.application.Shared.dtoMapper.AccountMapper;
import com.example.domain.Cash.cashRecord.CashRecord;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Cash.cashAccount.CashAccount;

import java.util.List;
import java.util.stream.Collectors;

public class CashAccountDTOMapper implements AccountMapper<CashAccount, CashAccountDTO> {
    @Override
    public CashAccountDTO mapToDto(CashAccount cashAccount) {
        return new CashAccountDTO(
                cashAccount.tripId.toString(),
                cashAccount.mTotalBalance.toString(),
                this.mapAccountRecord(cashAccount.mRecords)
        );
    }

    // region helper method ------------------------------------------------------------------------
    private List<CashAccountDTO.Record> mapAccountRecord(List<CashAccount.Record> records) {
        return records.stream()
                .map(record -> new CashAccountDTO.Record(
                        record.mDate.getMillisecond(),
                        record.mNote.toString(),
                        record.mAmount.toString(),
                        record.mSourceTransId.toString(),
                        this.mapSourceType(record.mSource),
                        this.mapEffect(record.mSource)))
                .collect(Collectors.toList());
    }

    private AccountRecordDTO.Effect mapEffect(CashRecord.SourceType source) {
        return AccountRecordDTO.Effect.valueOf(source);
    }

    private CashAccountDTO.SourceType mapSourceType(CashRecord.SourceType source) {
        switch (source) {
            case TRIP_EXPENSE:
                return CashAccountDTO.SourceType.TRIP_EXPENSE;
            case CASH_TRANS_DEPOSIT:
                return CashAccountDTO.SourceType.CASH_DEPOSIT;
            case CASH_TRANS_WITHDRAWAL:
                return CashAccountDTO.SourceType.CASH_WITHDRAWAL;
            case CASH_TRANS_ADJUST_UP:
                return CashAccountDTO.SourceType.CASH_ADJUST_UP;
            case CASH_TRANS_ADJUST_DOWN:
                return CashAccountDTO.SourceType.CASH_ADJUST_DOWN;
            case COLL_TRANS_SETTLEMENT_MADE:
                return CashAccountDTO.SourceType.COLL_SETTLEMENT_MADE;
            case COLL_TRANS_SETTLEMENT_ACCEPTED:
                return CashAccountDTO.SourceType.COLL_SETTLEMENT_ACCEPTED;
            case COLL_TRANS_CONTRIBUTION_MADE:
                return CashAccountDTO.SourceType.COLL_CONTRIBUTION_MADE;
            case COLL_TRANS_CONTRIBUTION_ACCEPTED:
                return CashAccountDTO.SourceType.COLL_CONTRIBUTION_ACCEPTED;
            default:
                throw new UnexpectedEnumValue();
        }
    }
    // endregion helper method ---------------------------------------------------------------------
}
