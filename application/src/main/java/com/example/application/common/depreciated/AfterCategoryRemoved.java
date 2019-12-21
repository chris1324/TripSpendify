package com.example.application.common.depreciated;

import com.example.application.common.repository.BudgetBookRepository;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Budget.budgetBook.BudgetTransactionService;
import com.example.domain.Budget.budgetTransaction.BudgetTransaction;
import com.example.domain.Common.domaineEventBus.DomainEventEnum;
import com.example.domain.Common.domaineEventBus.DomainEventHandler;
import com.example.domain.Trip.tripEvent.CategoryRemoved;

import java.util.List;


@Deprecated
public class AfterCategoryRemoved extends DomainEventHandler<CategoryRemoved> {

    // region Variables and Constructor ------------------------------------------------------------
    private final BudgetBookRepository mBudgetBookRepository;
    private final BudgetTransactionService mBudgetTransactionService;

    public AfterCategoryRemoved(BudgetBookRepository budgetBookRepository,
                                BudgetTransactionService budgetTransactionService) {
        super(DomainEventEnum.CATEGORY_REMOVED);
        mBudgetBookRepository = budgetBookRepository;
        mBudgetTransactionService = budgetTransactionService;
    }


    // endregion Variables and Constructor ---------------------------------------------------------
    @Override
    public void onEventDispatched(CategoryRemoved domainEvent) {
        // Prepare
        BudgetBook budgetBook = mBudgetBookRepository.getByTripBkId(domainEvent.tripId);
        List<BudgetTransaction> allCategoryTrans = budgetBook
                .getBudgetTransactions()
                .searchReturnAll(budgetTransaction -> budgetTransaction.getCategoryId() == domainEvent.categoryId);

        // Remove
        for (BudgetTransaction budgetTrans : allCategoryTrans) {
            mBudgetTransactionService.removeBudgetTrans(budgetBook, budgetTrans.getId());
        }
    }
}
