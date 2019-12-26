package com.example.application.Budget.budgetTransaction.usecase;

import com.example.application.Budget.budgetBook.BudgetBookRepository;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Budget.budgetBook.BudgetTransactionService;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.id.ID;

public class RemoveBudgetTrans extends CommandUseCase<RemoveBudgetTrans.RequestDTO, UseCase.Void> {

    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetBookRepository mBudgetBookRepository;
    private final BudgetTransactionService mBudgetTransactionService;

    public RemoveBudgetTrans(BudgetBookRepository budgetBookRepository, BudgetTransactionService budgetTransactionService) {
        mBudgetBookRepository = budgetBookRepository;
        mBudgetTransactionService = budgetTransactionService;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // region RequestDTO  ------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
        final String tripBookId;
        final String budgetTransId;

        public RequestDTO(String tripBookId, String budgetTransId) {
            this.tripBookId = tripBookId;
            this.budgetTransId = budgetTransId;
        }
    }
    // endregion RequestDTO  ---------------------------------------------------------------------

    @Override
    protected Outcome<Void> execute(RequestDTO requestDTO) {
        BudgetBook budgetBook = mBudgetBookRepository.getByTripId(ID.existingId(requestDTO.tripBookId));
        mBudgetTransactionService.removeBudgetTrans(budgetBook, ID.existingId(requestDTO.budgetTransId));
        return Outcome.ok();
    }

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------
}
