package com.example.application.common.repository;

import com.example.domain.Collectible.collectibleBook.CollectibleBook;
import com.example.domain.Common.sharedValueObject.id.ID;

public interface CollectibleBookRepository extends Repository<CollectibleBook> {

    CollectibleBook getByTripBkId(ID tripBookId);

    void removeByTripBkID(ID tripBkId);
}
