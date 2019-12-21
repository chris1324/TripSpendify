package com.example.application.common.repository;

import com.example.domain.Budget.budgetBook.BudgetBook;
import com.example.domain.Common.sharedValueObject.id.ID;

public interface BudgetBookRepository extends Repository<BudgetBook> {

    BudgetBook getByTripBkId(ID tripBookId);

    void removeByTripBkID(ID tripBkId);
}
