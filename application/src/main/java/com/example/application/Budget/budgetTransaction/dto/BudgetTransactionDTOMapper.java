package com.example.application.Budget.budgetTransaction.dto;

import com.example.application.Shared.dtoMapper.DtoMapper;
import com.example.domain.Budget.budgetTransaction.BudgetTransaction;

public class BudgetTransactionDTOMapper implements DtoMapper<BudgetTransaction,BudgetTransactionDTO> {
    @Override
    public BudgetTransactionDTO mapToDto(BudgetTransaction budgetTrans) {
        return new BudgetTransactionDTO(
                budgetTrans.getId().toString(),
                budgetTrans.getDate().getMillisecond(),
                budgetTrans.getNote().toString(),
                budgetTrans.getAmount().toString(),
                budgetTrans.getCategoryId().toString()
        );
    }
}
