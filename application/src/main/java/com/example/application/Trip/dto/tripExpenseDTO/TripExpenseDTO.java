package com.example.application.Trip.dto.tripExpenseDTO;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

import java.util.Map;

public class TripExpenseDTO implements UseCase.RequestDTO, UseCase.ResponseDTO{

    public final String tripExpenseId;
    public final String categoryId;
    public final long date;
    public final String note;

    public final String totalExpense;
    public final boolean isUnpaid;

    public final String amountPaidByUser;
    public final String amountSpentByUser;
    public final Map<String, String> amountPaidByMember;
    public final Map<String, String> amountSpentByMember;

    public TripExpenseDTO(String tripExpenseId,
                          String categoryId,
                          long date,
                          String note,
                          String totalExpense,
                          boolean isUnpaid,
                          String amountPaidByUser,
                          String amountSpentByUser,
                          Map<String, String> amountPaidByMember,
                          Map<String, String> amountSpentByMember) {
        this.tripExpenseId = tripExpenseId;
        this.categoryId = categoryId;
        this.date = date;
        this.note = note;
        this.totalExpense = totalExpense;
        this.isUnpaid = isUnpaid;
        this.amountPaidByUser = amountPaidByUser;
        this.amountSpentByUser = amountSpentByUser;
        this.amountPaidByMember = amountPaidByMember;
        this.amountSpentByMember = amountSpentByMember;
    }
}
