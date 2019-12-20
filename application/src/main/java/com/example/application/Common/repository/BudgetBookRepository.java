package com.example.application.Common.repository;

import com.example.domain.Budget.budgetbook.BudgetBook;
import com.example.domain.Common.sharedvalueobject.id.ID;

public interface BudgetBookRepository extends Repository<BudgetBook> {

    BudgetBook getByTripBkId(ID tripBookId);

    void removeByTripBkID(ID tripBkId);
}
