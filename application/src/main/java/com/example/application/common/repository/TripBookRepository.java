package com.example.application.common.repository;

import com.example.domain.Common.sharedValueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

public interface TripBookRepository extends Repository<TripBook> {

    TripBook getTripBookById(ID tripBookId);
}
