package com.example.application.Common.repository;

import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Trip.tripbook.TripBook;

public interface TripBookRepository extends Repository<TripBook> {

    TripBook getTripBookById(ID tripBookId);
}
