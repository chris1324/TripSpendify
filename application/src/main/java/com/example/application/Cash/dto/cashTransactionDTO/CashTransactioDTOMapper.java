package com.example.application.Cash.dto.cashTransactionDTO;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.domain.Cash.cashTransaction.CashTransaction;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;

public class CashTransactioDTOMapper implements DtoMapper<CashTransaction, CashTransactionDTO> {

    @Override
    public CashTransactionDTO mapToDto(CashTransaction cashTransaction) {
        return new CashTransactionDTO(
                cashTransaction.getId().toString(),
                cashTransaction.getDate().getMillisecond(),
                cashTransaction.getNote().toString(),
                cashTransaction.getAmount().toString(),
                this.getTransType(cashTransaction)
        );
    }

    private CashTransactionDTO.TransType getTransType(CashTransaction cashTransaction) {
        switch (cashTransaction.getTransactionType()) {
            case WITHDRAWAL:
                return CashTransactionDTO.TransType.WITHDRAWAL;
            case DEPOSIT:
                return CashTransactionDTO.TransType.DEPOSIT;
            case ADJUSTMENT_UP:
                return CashTransactionDTO.TransType.ADJUSTMENT_UP;
            case ADJUSTMENT_DOWN:
                return CashTransactionDTO.TransType.ADJUSTMENT_DOWN;
            default:
                throw new UnexpectedEnumValue();
        }
    }
}
