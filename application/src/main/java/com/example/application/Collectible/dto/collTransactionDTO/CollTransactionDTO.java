package com.example.application.Collectible.dto.collTransactionDTO;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

import javax.annotation.Nullable;

public class CollTransactionDTO implements UseCase.RequestDTO, UseCase.ResponseDTO {

    public enum TransType {
        CONTRIBUTION,
        SETTLEMENT
    }

    public enum Payer {
        USER,
        MEMBER
    }

    public final String collTransId;
    public final long date;
    public final String note;
    public final String amount;
    public final TransType mTransType;

    public final String involvedMemberId;
    public final Payer mPayer;

    public CollTransactionDTO(String collTransId,
                              long date,
                              String note,
                              String amount,
                              TransType transType,
                              String involvedMemberId,
                              Payer payer) {
        this.collTransId = collTransId;
        this.date = date;
        this.note = note;
        this.amount = amount;
        mTransType = transType;
        this.involvedMemberId = involvedMemberId;
        mPayer = payer;
    }
}
