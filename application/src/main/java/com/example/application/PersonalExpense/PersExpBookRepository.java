package com.example.application.PersonalExpense;

import com.example.application.Shared.repository.CommandModelRepository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.PersernalExpense.persExpBook.PersExpBook;

public interface PersExpBookRepository extends CommandModelRepository<PersExpBook> {

    PersExpBook getByTripId(ID tripBookID);
}
