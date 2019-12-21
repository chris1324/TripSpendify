package com.example.application.common.repository;

import com.example.domain.Cash.cashBook.CashBook;
import com.example.domain.Common.sharedValueObject.id.ID;

public interface CashBookRepository extends Repository<CashBook> {

    CashBook getByTripBkId(ID tripBookID);

    void removeByTripBkID(ID tripBkId);
}
