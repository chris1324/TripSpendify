package com.example.application.common.repository;

import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.PersExpense.persExpBook.PersExpBook;

public interface PersExpBookRepository extends Repository<PersExpBook> {

    void removeByTripBkID(ID tripBkId);
    PersExpBook getByTripBkId(ID tripBookID);
}
