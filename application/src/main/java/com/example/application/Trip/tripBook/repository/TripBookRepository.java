package com.example.application.Trip.tripBook.repository;

import com.example.application.Shared.repository.CommandModelRepository;
import com.example.application.Trip.tripBook.minimal.TripBookMinimal;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Trip.tripBook.TripBook;

import java.util.List;

import io.reactivex.Observable;

public interface TripBookRepository extends CommandModelRepository<TripBook> {

    void remove(ID id);

    // ---------------------------------------------------------------------------------------------

    Observable<TripBook> fetchById(ID tripBookId);

    TripBook getById(ID tripBookId);

    // ---------------------------------------------------------------------------------------------

    Observable<TripBookMinimal> fetchMinimalById(ID tripBookId);

    Observable<List<TripBookMinimal>> fetchAllMinimal();

}
