package com.example.application.Trip.repository;

import com.example.application.Shared.repository.CommandModelRepository;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.List;

import io.reactivex.Observable;

public interface TripBookRepository extends CommandModelRepository<TripBook> {

    Observable<TripBook> fetchById(ID tripBookId);
    
    Observable<List<TripBook>> fetchAll();

    TripBook getById(ID tripBookId);
}
