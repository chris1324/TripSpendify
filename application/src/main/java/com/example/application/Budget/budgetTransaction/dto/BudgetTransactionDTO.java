package com.example.application.Budget.budgetTransaction.dto;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

public class BudgetTransactionDTO implements UseCase.RequestDTO, UseCase.ResponseDTO {

    public final String budgetTransId;
    public final long date;
    public final String note;
    public final String amount;
    public final String categoryId;

    public BudgetTransactionDTO(String budgetTransId, long date, String note, String amount, String categoryId) {
        this.budgetTransId = budgetTransId;
        this.date = date;
        this.note = note;
        this.amount = amount;
        this.categoryId = categoryId;
    }
}
