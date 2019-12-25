package com.example.application.Cash.repository;

import com.example.application.Shared.repository.CommandModelRepository;
import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public interface CashBookRepository extends CommandModelRepository<CashBook> {

    Observable<CashBook> fetchByTripId(ID tripBookID);

    CashBook getByTripId(ID tripBookID);


}
