package com.example.application.Common.repository;

import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.PersExpense.persexpbook.PersExpBook;

public interface PersExpBookRepository extends Repository<PersExpBook> {

    void removeByTripBkID(ID tripBkId);
    PersExpBook getByTripBkId(ID tripBookID);
}
