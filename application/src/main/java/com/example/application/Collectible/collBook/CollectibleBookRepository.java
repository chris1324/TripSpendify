package com.example.application.Collectible.collBook;

import com.example.application.Shared.repository.CommandModelRepository;
import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Shared.valueObject.id.ID;

import io.reactivex.Observable;

public interface CollectibleBookRepository extends CommandModelRepository<CollectibleBook> {

    Observable<CollectibleBook> fetchByTripId(ID tripBookId);

    CollectibleBook getByTripId(ID tripBookId);

}
