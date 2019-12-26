package com.example.application.Cash.cashAccount.repository;

import com.example.application.Shared.repository.Repository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Cash.cashAccount.CashAccount;

import io.reactivex.Observable;

public interface CashAccountRepository extends Repository {

    Observable<CashAccount> fetchByTripId(ID tripId);
}
