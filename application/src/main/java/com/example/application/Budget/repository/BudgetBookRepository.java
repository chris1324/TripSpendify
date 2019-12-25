package com.example.application.Budget.repository;

import com.example.application.Shared.repository.CommandModelRepository;
import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Shared.valueObject.id.ID;

public interface BudgetBookRepository extends CommandModelRepository<BudgetBook> {

    BudgetBook getByTripId(ID tripBookId);


}
