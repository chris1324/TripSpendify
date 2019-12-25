package com.example.application.Budget.repository;

import com.example.application.Shared.repository.Repository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Budget.budgetAccount.BudgetAccount;

import io.reactivex.Observable;

public interface BudgetAccountRepository extends Repository {

    Observable<BudgetAccount> fetchByTripId(ID tripId);
}
