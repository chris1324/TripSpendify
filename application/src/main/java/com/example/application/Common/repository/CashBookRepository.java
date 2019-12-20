package com.example.application.Common.repository;

import com.example.domain.Cash.cashbook.CashBook;
import com.example.domain.Common.sharedvalueobject.id.ID;

public interface CashBookRepository extends Repository<CashBook> {

    CashBook getByTripBkId(ID tripBookID);

    void removeByTripBkID(ID tripBkId);
}
