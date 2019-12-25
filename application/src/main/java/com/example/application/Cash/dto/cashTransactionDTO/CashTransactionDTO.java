package com.example.application.Cash.dto.cashTransactionDTO;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

public class CashTransactionDTO implements UseCase.RequestDTO,UseCase.ResponseDTO {
    public enum TransType {
        WITHDRAWAL,
        DEPOSIT,
        ADJUSTMENT_UP,
        ADJUSTMENT_DOWN
    }

    public final String cashTransId;
    public final long date;
    public final String note;
    public final String amount;
    public final TransType mTransType;

    public CashTransactionDTO(String cashTransId, long date, String note, String amount, TransType transType) {
        this.cashTransId = cashTransId;
        this.date = date;
        this.note = note;
        this.amount = amount;
        mTransType = transType;
    }
}
